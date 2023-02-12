package com.mythostrike.model.game.management;

import com.mythostrike.controller.GameController;
import com.mythostrike.controller.message.game.ChampionSelectionMessage;
import com.mythostrike.model.game.Game;
import com.mythostrike.model.game.Phase;
import com.mythostrike.model.game.activity.Activity;
import com.mythostrike.model.game.activity.Card;
import com.mythostrike.model.game.activity.cards.CardPile;
import com.mythostrike.model.game.activity.events.handle.CardDrawHandle;
import com.mythostrike.model.game.activity.system.NextPhase;
import com.mythostrike.model.game.activity.system.PickRequest;
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
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;

@Slf4j
public class GameManager {

    //player has 3 champions to pick in game, god-king has 5
    public static final int PICK_CHAMPION_COUNT = 3;
    public static final int PICK_CHAMPION_COUNT_GOD_KING = 5;
    //player start up with 4 cards and draw 2 cards at each turn start
    public static final int CARD_COUNT_START_UP = 4;
    public static final int CARD_COUNT_TURN_START = 2;
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
    private final GameController gameController;
    @Getter
    private final Queue<Activity> currentActivity;
    @Getter
    @Setter
    private Phase phase;
    private boolean proceeding;
    private PickRequest lastPickRequest;

    public GameManager(List<Player> players, Mode mode, int lobbyId, GameController gameController) {
        game = new Game(players, mode, this);
        cardManager = new CardManager(this);
        eventManager = new EventManager(this);
        playerManager = new PlayerManager(this);
        currentActivity = new LinkedList<>();
        this.lobbyId = lobbyId;
        this.gameController = gameController;
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

    //----------------GameRun----------------

    //----------------GameStart----------------
    public void gameStart() {

        List<Player> players = game.getAlivePlayers();
        players.forEach(player -> player.initialize(this));
        //TODO:unterschiedliche dinge an frontend schicken
        //identityDistribution(players);
        //TODO:extra panel machen
        selectChampionPhase(players);
    }

    private void selectChampionPhase(List<Player> players) {
        for (Player player : players) {
            List<Champion> championList = new ArrayList<>(ChampionList.getChampionList().getChampions());
            Collections.shuffle(championList, Game.RANDOM_SEED);

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
                = new ChampionSelectionMessage(player.getIdentity(), list);

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
            output(player.getUsername() + " as Seat " + players.indexOf(player) + " has "
                + player.getChampion().getName() + " with skill:");

            cardManager.drawCard(new CardDrawHandle(this, "Draw 4 cards at game start",
                player, CARD_COUNT_START_UP, game.getDrawPile()));
        }

        phase = Phase.ROUND_START;
        proceed();
    }

    public void proceed() {
        proceeding = true;

        while (proceeding) {
            Activity activity = currentActivity.peek();
            if (activity == null) {
                activity = new NextPhase(this);
                currentActivity.add(activity);
            }

            //TODO: maybe remove instanceof
            if (activity instanceof PickRequest pickRequest) {
                proceeding = false;
                lastPickRequest = pickRequest;
            }
            if (game.isGameOver()) {
                gameOver();
                proceeding = false;
                return;
            }
            runActivity(activity);
        }
    }

    private void runActivity(@NotNull Activity activity) {
        activity.use();
        //if activity is finished, remove it from queue
        if (activity.end()) {
            currentActivity.poll();
        }
    }

    public void cleanTable() {
        StringBuilder hint = new StringBuilder("Cards from TableDeck after calculation will get into ThrowDeck :");
        CardPile throwDeck = game.getThrowPile();
        CardPile tableDeck = game.getTablePile();
        for (Card card : tableDeck.getCards()) {
            hint.append(card.toString()).append(",");

            throwDeck.getCards().add(card);
        }
        tableDeck.getCards().clear();
        debug(hint.toString());
    }

    public void queueActivity(Activity activity) {
        currentActivity.add(activity);
    }

    public void gameOver() {
        //TODO implement with frontend panel
    }

    public void highlightPickRequest(PickRequest pickRequest) {
        gameController.highlight(lobbyId, pickRequest.getPlayer().getUsername(), pickRequest.getHighlightMessage());
        lastPickRequest = pickRequest;
    }

    public void selectChampion(String playerName, Champion champion) {
        playerManager.initialChampions(champion, getPlayerByName(playerName));

        //if all players selected a champion, start game
        if (game.getAlivePlayers().stream().map(Player::getChampion).allMatch(Objects::nonNull)) {
            cardDistribution();
        }
    }

    public void selectCards(String playerName, List<Card> cards) {
        Player player = getPlayerByName(playerName);
        if (lastPickRequest != null && lastPickRequest.getPlayer().equals(player)) {
            lastPickRequest.setSelectedCards(cards);
            lastPickRequest = null;
            proceed();
        }
    }

    public void selectPlayers(String playerName, List<String> targetUsernames) {
        Player player = getPlayerByName(playerName);

        List<Player> targets = new ArrayList<>(targetUsernames.stream().map(this::getPlayerByName).toList());
        if (lastPickRequest != null && lastPickRequest.getPlayer().equals(player)) {
            lastPickRequest.setSelectedPlayers(targets);
            lastPickRequest = null;
            proceed();
        }
    }

    public void cancelRequest(String playerName) {
        Player player = getPlayerByName(playerName);
        if (lastPickRequest != null && lastPickRequest.getPlayer().equals(player)) {
            lastPickRequest.setClickedCancel(true);
            proceed();
        }
    }

    public Player getPlayerByName(String playerName) {
        for (Player player : game.getAllPlayers()) {
            if (player.getUsername().equals(playerName)) {
                return player;
            }
        }
        return null;
    }

    //debug
    public void debug(String msg) {
        log.debug(msg);
    }

    public void output(String output) {
        gameController.logMessage(lobbyId, output);
    }
}
