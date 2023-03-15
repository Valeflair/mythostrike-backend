package com.mythostrike.model.game.management;

import com.mythostrike.MythostrikeBackendApplication;
import com.mythostrike.controller.GameController;
import com.mythostrike.controller.message.game.HighlightMessage;
import com.mythostrike.controller.message.game.PlayerCondition;
import com.mythostrike.controller.message.game.PlayerGameResult;
import com.mythostrike.controller.message.lobby.ChampionMessage;
import com.mythostrike.controller.message.lobby.ChampionSelectionMessage;
import com.mythostrike.model.exception.IllegalInputException;
import com.mythostrike.model.game.Game;
import com.mythostrike.model.game.Phase;
import com.mythostrike.model.game.activity.ActiveSkill;
import com.mythostrike.model.game.activity.Activity;
import com.mythostrike.model.game.activity.PassiveSkill;
import com.mythostrike.model.game.activity.cards.Card;
import com.mythostrike.model.game.activity.cards.CardPile;
import com.mythostrike.model.game.activity.events.handle.CardDrawHandle;
import com.mythostrike.model.game.activity.events.handle.CardMoveHandle;
import com.mythostrike.model.game.activity.system.CheckDying;
import com.mythostrike.model.game.activity.system.NextPhase;
import com.mythostrike.model.game.activity.system.PickCardToPLay;
import com.mythostrike.model.game.activity.system.PickRequest;
import com.mythostrike.model.game.activity.system.PlayCard;
import com.mythostrike.model.game.activity.system.phase.ActiveTurn;
import com.mythostrike.model.game.activity.system.phase.RoundStartTurn;
import com.mythostrike.model.game.player.Bot;
import com.mythostrike.model.game.player.Champion;
import com.mythostrike.model.game.player.ChampionList;
import com.mythostrike.model.game.player.Player;
import com.mythostrike.model.lobby.Identity;
import com.mythostrike.model.lobby.Mode;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class GameManager {

    private static final int SLEEP_BEFORE_CARD_DISTRIBUTION = MythostrikeBackendApplication.TEST_MODE ? 0 : 100;
    //player has 3 champions to pick in game, god-king has 5
    private static final int PICK_CHAMPION_COUNT = 3;
    private static final int PICK_CHAMPION_COUNT_GOD_KING = 5;
    //player start up with 4 cards and draw 2 cards at each turn start
    private static final int CARD_COUNT_START_UP = 4;
    private static final ExecutorService EXECUTOR = Executors.newSingleThreadExecutor();
    public static final int NO_SKILL_SELECTED = -1;
    @Getter
    private final Game game;
    @Getter
    private final CardManager cardManager;
    @Getter
    private final EventManager eventManager;
    @Getter
    private final PlayerManager playerManager;
    @Getter
    private final int lobbyId;
    @Getter
    private final Random random;
    @Getter
    private final GameController gameController;
    @Getter
    private final Deque<Activity> currentActivity;
    @Getter
    @Setter
    private Phase phase;
    private boolean proceeding;
    private PickRequest lastPickRequest;


    public GameManager(List<Player> players, Mode mode, int lobbyId, GameController gameController, Random random) {
        this.random = random;
        this.lobbyId = lobbyId;
        this.gameController = gameController;
        game = new Game(players, mode, this);
        cardManager = new CardManager(this);
        eventManager = new EventManager(this);
        playerManager = new PlayerManager(this);
        currentActivity = new LinkedList<>();
    }

    //---------------compiling method----------
    public static List<Integer> convertCardsToInteger(List<Card> cards) {
        List<Integer> cardIds = new ArrayList<>();
        for (Card card : cards) {
            cardIds.add(card.getId());
        }
        return cardIds;
    }

    public static List<String> convertPlayersToUsername(List<Player> players) {
        List<String> playerNames = new ArrayList<>();
        for (Player player : players) {
            playerNames.add(player.getUsername());
        }
        return playerNames;
    }

    private List<Player> convertUserNameToPlayers(@NotNull List<String> playerNames) {
        List<Player> players = new ArrayList<>();
        for (String name : playerNames) {
            players.add(game.getAllPlayers().stream().filter(player -> player.getUsername().equals(name)).findFirst()
                .orElse(null));
        }
        return players;
    }

    public List<Card> convertIdToCards(List<Integer> cardIds) {

        return game.getAllCards().getCards().stream().filter(card -> cardIds.contains(card.getId())).toList();
    }

    /**
     * check parameters from a REST call before adding the activity to the queue to do async. That way there won't be
     * any errors when the activity is actually executed and the REST call will finish faster for the client.
     * After the runnable is finished, the game will be updated with updateGame(lobbyId).
     *
     * @param runnable the activity to be executed
     */
    private void submitRunnable(Runnable runnable) {
        EXECUTOR.execute(() -> {
            try {
                runnable.run();
            } catch (Exception exception) {
                List<StackTraceElement> limitedStackTrace = Arrays.stream(exception.getStackTrace()).limit(5).toList();
                log.error("Error in runnable: '{}' at '{}'", exception.getMessage(), limitedStackTrace);
            }
            gameController.updateGame(lobbyId);
        });
    }

    /**
     * check parameters from a REST call before adding the activity to the queue to do async. That way there won't be
     * any errors when the activity is actually executed and the REST call will finish faster for the client.
     *
     * @param runnable the activity to be executed
     */
    private void submitRunnableWithoutUpdate(Runnable runnable) {
        EXECUTOR.execute(() -> {
            try {
                runnable.run();
            } catch (Exception exception) {
                List<StackTraceElement> limitedStackTrace = Arrays.stream(exception.getStackTrace()).limit(5).toList();
                log.error("Error in runnable: '{}' at '{}'", exception.getMessage(), limitedStackTrace);
            }
        });
    }

    //----------------GameRun----------------
    public void endTurn(String username) {
        if (!game.getCurrentPlayer().getUsername().equals(username)) {
            throw new IllegalInputException("Player " + username + " is not the current player");
        }
        if (currentActivity.getFirst() == null) {
            throw new IllegalInputException("no Current Activity to run");
        }
        if (!phase.equals(Phase.ACTIVE_TURN)) {
            throw new IllegalInputException("not in an active turn");
        }

        List<String> removeList = List.of(PickRequest.NAME, PlayCard.NAME, PickCardToPLay.NAME, ActiveTurn.NAME);
        for (String name : removeList) {
            if (currentActivity.isEmpty()) {
                break;
            }
            if (currentActivity.getFirst().getName().equals(name)) {
                currentActivity.removeFirst();
            }
        }

        submitRunnable(this::proceed);
    }
    //----------------GameStart----------------

    /**
     * has to be called when the lobby owner starts the game.
     * it starts the ChampionSelectionPhase and sends to each player the list of champions to pick from.
     * The game isn't started until all players have picked their champions.
     * When all players subscribed to the game websocket the methode allPlayersConnected() is called and from there
     */
    public void gameStart() {


        List<Player> players = game.getAlivePlayers();
        //bot needs the gameManager to be set
        players.forEach(player -> player.initialize(this));

        CheckDying checkDying;
        for (Player player : players) {
            checkDying = new CheckDying();
            checkDying.register(eventManager, player);
        }

        submitRunnableWithoutUpdate(() -> selectChampionPhase(players));
    }

    /**
     * has to be called when all players are connected to the game websocket.
     * starts the actual game procedure and distributes the cards to the players.
     * After that the first turn is started and to the first player a highlight message is sent.
     */
    public void allPlayersConnected() {
        //frontend needs a bit of time to load the game for the last user
        try {
            Thread.sleep(SLEEP_BEFORE_CARD_DISTRIBUTION);
        } catch (InterruptedException e) {
            log.warn("sleep was interrupted");
        }
        submitRunnable(this::cardDistribution);
    }

    private void selectChampionPhase(List<Player> players) {
        for (Player player : players) {
            List<Champion> championList = new ArrayList<>(ChampionList.getChampionList().getChampions());
            Collections.shuffle(championList, this.random);

            List<Champion> list = new ArrayList<>();
            //ask player to pick champion
            int championCount = PICK_CHAMPION_COUNT;
            if (player.getIdentity().equals(Identity.GOD_KING)) {
                championCount = PICK_CHAMPION_COUNT_GOD_KING;
            }

            //liste aufstellen
            while (list.size() < championCount && !championList.isEmpty()) {
                list.add(championList.remove(0));
            }
            //wahl aussuchen und Leben initialisieren
            ChampionSelectionMessage championSelectionMessage
                = new ChampionSelectionMessage(player.getIdentity(), list.stream().map(ChampionMessage::new).toList());

            if (player instanceof Bot bot) {
                bot.selectChampionFrom(championSelectionMessage);
            } else {
                gameController.selectChampionFrom(lobbyId, player.getUsername(), championSelectionMessage);
            }
        }
    }

    private void cardDistribution() {
        List<Player> players = game.getAlivePlayers();

        //initial Cards for player
        output("Game Started, Player has following champions:");
        for (Player player : players) {
            StringBuilder hint = new StringBuilder(
                "%s at Seat %d has %s with skills:".formatted(player.getUsername(), players.indexOf(player),
                    player.getChampion().getName()));
            for (ActiveSkill skill : player.getChampion().getActiveSkills()) {
                hint.append(String.format("%n%s", skill.getName()));
            }

            for (PassiveSkill skill : player.getChampion().getPassiveSkills()) {
                hint.append(String.format("%n%s", skill.getName()));
            }

            output(hint.toString());

            cardManager.drawCard(new CardDrawHandle(this, "Draw 4 cards at game start",
                player, CARD_COUNT_START_UP, game.getDrawPile()));
        }

        phase = Phase.ROUND_START;
        queueActivity(new RoundStartTurn(this));
        proceed();
    }

    private void proceed() {
        proceeding = true;

        while (proceeding) {
            if (currentActivity.isEmpty()) {
                currentActivity.addFirst(new NextPhase(this));
            }
            Activity activity = currentActivity.getFirst();
            debug("running activity:" + activity.getName());
            runActivity(activity);
        }
    }

    private void runActivity(@NotNull Activity activity) {
        activity.use();
        //if activity is finished, remove it from queue
        if (activity.end()) {
            currentActivity.remove(activity);
        }
    }

    public void cleanTable() {
        if (game.getTablePile().getCards().isEmpty()) {
            return;
        }
        StringBuilder hint = new StringBuilder("Cards from TableDeck after calculation will get into ThrowDeck :");
        CardPile discardDeck = game.getDiscardPile();
        CardPile tableDeck = game.getTablePile();
        for (Card card : tableDeck.getCards()) {
            hint.append(card.toString()).append(",");
        }
        List<Card> cards = tableDeck.getCards();
        CardMoveHandle cardMoveHandle = new CardMoveHandle(this,
            "Move cards from tableDeck to throwDeck", null, null, tableDeck, discardDeck, cards);
        cardManager.moveCard(cardMoveHandle);
        debug(hint.toString());
        gameController.updateGame(lobbyId);
    }

    public void queueActivity(Activity activity) {
        currentActivity.addFirst(activity);
    }

    public void checkGameOver() {

        //check if anybody won
        List<Player> winners = new ArrayList<>();
        List<Player> losers = new ArrayList<>();
        for (Player player : game.getAllPlayers()) {
            if (player.getIdentity().hasWon(player, this)) {
                winners.add(player);
            } else {
                losers.add(player);
            }
        }

        //if nobody won, the game isn't over and the next round can start
        if (winners.isEmpty()) return;

        gameController.updateGame(this.lobbyId);

        //if somebody won, the game is over and the winner gets the rewards
        List<PlayerGameResult> results = new ArrayList<>();
        for (Player winner : winners) {
            winner.addWinRewards();
            results.add(new PlayerGameResult(winner, true));
        }
        for (Player loser : losers) {
            loser.deductLoosePenalty();
            results.add(new PlayerGameResult(loser, false));
        }
        gameController.gameEnd(lobbyId, results);
        //stop game
        proceeding = false;
    }

    public void highlightPickRequest(PickRequest pickRequest) {
        lastPickRequest = pickRequest;
        proceeding = false;
        if (pickRequest.getPlayer() instanceof Bot bot) {
            bot.highlight(pickRequest);
        } else {
            gameController.highlight(lobbyId, pickRequest.getPlayer().getUsername(), pickRequest.getHighlightMessage());
        }
    }

    public void selectChampion(String playerName, Champion champion) {
        Player player = getPlayerByName(playerName);

        submitRunnable(() -> {
            playerManager.initializeChampionForPlayer(champion, player);
            if (game.getAlivePlayers().stream().allMatch(player1 -> player1.getChampion() != null)) {
                log.debug("All players have selected their champion");
            }
        });

        //cardDistribution() is started when all players connected to the inGame (/games/{id}/{username}) Websocket
        //the methode handleSessionSubscribeEvent in GameController is called when a client connects to the websocket.
        //If all clients are connected, then the methode cardDistribution() is called.
    }

    private static boolean isTargetSelectionValid(int index, List<PlayerCondition> playerConditions, List<String> targets) {
        Set<String> allowedPlayers;
        if (playerConditions.size() > index) {
            allowedPlayers
                = new HashSet<>(playerConditions.get(index).players());
        } else {
            allowedPlayers = new HashSet<>();
        }
        return allowedPlayers.containsAll(targets);
    }

    public void selectCards(String playerName, List<Integer> cardIds, List<String> targets) {
        List<Card> cards = convertIdToCards(cardIds);
        List<Player> targetPlayers = convertUserNameToPlayers(targets);
        Player player = getPlayerByName(playerName);

        if (lastPickRequest == null || !lastPickRequest.getPlayer().equals(player)) {
            throw new IllegalArgumentException("Player is not allowed to select cards");
        }
        HighlightMessage highlightMessage = lastPickRequest.getHighlightMessage();

        //check if the player selected the valid cards
        if (!highlightMessage.cardCount().contains(cardIds.size())) {
            throw new IllegalArgumentException("Not allowed number of cards selected");
        }
        if (!new HashSet<>(highlightMessage.cardIds()).containsAll(cardIds)) {
            throw new IllegalArgumentException("Player selected not allowed cards");
        }

        lastPickRequest.setSelectedCards(cards);

        //check if the player selected the valid targets
        if (cards.size() == 1) {
            int index = highlightMessage.cardIds().indexOf(cardIds.get(0));
            if (!isTargetSelectionValid(index, highlightMessage.cardPlayerConditions(), targets)) {
                throw new IllegalArgumentException("Selected targets is not allowed for this card");
            }

            lastPickRequest.setSelectedPlayers(targetPlayers);
        } else {
            //only one card can be selected with targets
            if (!targets.isEmpty()) {
                throw new IllegalArgumentException("Only one card can be selected with targets");
            }
        }
        cards.forEach(card -> card.setPickRequest(lastPickRequest));
        lastPickRequest = null;

        submitRunnable(this::proceed);
    }

    public void selectSkill(String playerName, int skillId, List<String> targets) {
        Player player = getPlayerByName(playerName);
        List<Player> targetPlayers = convertUserNameToPlayers(targets);

        if (lastPickRequest == null || !lastPickRequest.getPlayer().equals(player)) {
            throw new IllegalArgumentException("Player is not allowed to select cards");
        }
        HighlightMessage highlightMessage = lastPickRequest.getHighlightMessage();

        //no skill selected
        if (skillId == NO_SKILL_SELECTED) {
            if (!highlightMessage.skillCount().contains(0)) {
                throw new IllegalArgumentException("You have to select a skill");
            }
            lastPickRequest.setClickedCancel(true);
            submitRunnable(this::proceed);
            return;
        }

        //check if the player selected a valid skill
        List<Integer> skillIds = highlightMessage.skillIds();
        if (skillId < 0) throw new IllegalArgumentException(String.format("Skill id '%d' is not allowed", skillId));
        if ( !highlightMessage.skillCount().contains(1) ) {
            throw new IllegalArgumentException("You can't play a skill");
        }
        if ( !skillIds.contains(skillId) ) {
            throw new IllegalArgumentException("You dont have the skill with this id " + skillId);
        }
        int skillIndex = skillIds.indexOf(skillId);


        //check if the player selected the valid players
        if (!isTargetSelectionValid(skillIndex, highlightMessage.cardPlayerConditions(), targets)) {
            throw new IllegalArgumentException("You don't have the selected skill");
        }

        lastPickRequest.setSelectedPlayers(targetPlayers);

        lastPickRequest.setClickedCancel(true);
        if (highlightMessage.activateEndTurn()) {
            for (ActiveSkill skill : player.getChampion().getActiveSkills()) {
                if (skill.getId() == skillId) {
                    lastPickRequest.setClickedCancel(false);
                    lastPickRequest.setSelectedActiveSkill(skill);
                    break;
                }
            }
        } else {
            for (PassiveSkill skill : player.getChampion().getPassiveSkills()) {
                if (skill.getId() == skillId) {
                    lastPickRequest.setClickedCancel(false);
                    lastPickRequest.setSelectedPassiveSkill(skill);
                    break;
                }
            }
        }

        submitRunnable(this::proceed);
    }

    public Player getPlayerByName(String playerName) {
        for (Player player : game.getAllPlayers()) {
            if (player.getUsername().equals(playerName)) {
                return player;
            }
        }
        throw new IllegalInputException("Player with name " + playerName + " not found");
    }

    //debug
    public void debug(String msg) {
        log.debug(msg);
    }

    public void output(String output) {
        gameController.logMessage(lobbyId, output);
    }
}
