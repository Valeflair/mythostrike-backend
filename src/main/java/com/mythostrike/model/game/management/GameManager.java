package com.mythostrike.model.game.management;

import com.mythostrike.model.game.activity.cards.CardPile;
import com.mythostrike.model.game.activity.events.handle.CardDrawHandle;
import com.mythostrike.model.game.activity.system.NextPhase;
import com.mythostrike.model.game.activity.system.PickRequest;
import com.mythostrike.model.game.Game;
import com.mythostrike.model.lobby.Mode;
import com.mythostrike.model.game.Phase;
import com.mythostrike.model.game.activity.Activity;
import com.mythostrike.model.game.activity.Card;
import com.mythostrike.model.game.player.Champion;
import com.mythostrike.model.lobby.Identity;
import com.mythostrike.model.game.player.Player;
import com.mythostrike.controller.GameController;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameManager {

    //player has 3 champions to pick in game, god-king has 5
    public static final int PICK_CHAMPION_COUNT = 3;
    public static final int PICK_CHAMPION_COUNT_GODKING = 5;
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
    @Autowired
    private final GameController gameController;
    @Getter
    private List<Activity> currentActivity;
    @Getter
    @Setter
    private Phase phase;
    @Getter
    private final int lobbyId;
    private boolean proceeding;


    public GameManager(List<Player> players, Mode mode, int lobbyId) {
        game = new Game(players, mode, this);
        cardManager = new CardManager(this);
        eventManager = new EventManager(this);
        playerManager = new PlayerManager(this);
        this.lobbyId = lobbyId;
    }

    //---------------compiling method----------
    public static List<Integer> convertCardsToInteger(List<Card> cards) {
        List<Integer> cardIds = new ArrayList<>();
        for (Card card : cards) {
            cardIds.add(card.getId());
        }
        return cardIds;
    }

    public static List<String> convertPlayersToInteger(List<Player> players) {
        List<String> playerNames = new ArrayList<>();
        for (Player player : players) {
            playerNames.add(player.getUsername());
        }
        return playerNames;
    }

    //debug
    public void debug(String hint) {
        System.out.println("D:" + hint);
    }

    //----------------GameStart----------------
    public void gameStart() {

        List<Player> players = game.getAlivePlayers();
        //TODO:unterschiedliche dinge an frontend schicken
        identityDistribution(players);
        //TODO:extra panel machen
        selectChampionPhase(players);


        //initial Cards for player
        game.output("Game Started, Player has following champions:");
        for (Player player : players) {
            game.output(
                player.getUsername() + " as Seat " + players.indexOf(player) + " has " + player.getChampion().getName()
                    + "with skill:");

            cardManager.drawCard(
                new CardDrawHandle(this, "Draw 4 cards at game start", player, CARD_COUNT_START_UP,
                    game.getDrawPile()));

        }


        phase = Phase.ROUNDSTART;
        proceed();

    }

    private void selectChampionPhase(List<Player> players) {
        List<Champion> championList = Champion.getChampionPatterns();
        for (Player player : players) {

            List<Champion> list = new ArrayList<>();
            //ask player to pick champion
            int championCount = PICK_CHAMPION_COUNT;
            if (player.getIdentity().equals(Identity.GODKING)) {
                championCount = PICK_CHAMPION_COUNT_GODKING;
            }
            //liste aufstellen
            while (list.size() < championCount) {
                Collections.shuffle(championList);
                if (!list.contains(championList.get(0))) {
                    list.add(championList.get(0));
                }
            }
            //wahl aussuchen und Leben initialisieren
            playerManager.initialChampions(playerPickChampionFromList(player, championList), player);
        }
    }

    //----------------GameRun----------------

    public Champion playerPickChampionFromList(Player player, List<Champion> championList) {
        //TODO adjust with API
        return null;
    }

    private void identityDistribution(List<Player> players) {
        Mode mode = game.getMode();
        //only shuffle in identitymode
        if (game.getMode().equals(Mode.IDENTITY)) {
            Collections.shuffle(players);
        }
        //player get its own identity
        for (int i = 0; i < players.size(); i++) {
            players.get(i).setIdentity(mode.getIdentities().get(i));
        }
        //set godking into the first place
        if (!players.get(0).getIdentity().equals(Identity.GODKING)
            && (mode.equals(Mode.IDENTITY))) {
            for (int i = 0; i < players.size(); i++) {
                if (players.get(i).getIdentity().equals(Identity.GODKING)) {
                    Player godking = players.get(i);
                    players.set(i, players.get(0));
                    players.set(0, godking);
                }
            }
        }
    }

    public void proceed() {
        proceeding = true;

        while (proceeding) {
            if (currentActivity.isEmpty()) {
                currentActivity.add(new NextPhase(this));
            }
            Activity activity = currentActivity.get(0);
            if (activity.getName().equals(PickRequest.NAME)) {
                proceeding = false;
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
        if (activity.end()) {
            currentActivity.remove(activity);
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
        currentActivity.add(0, activity);
    }

    public void queueActivity(int pos, Activity activity) {
        currentActivity.add(pos, activity);
    }

    public void gameOver() {
        //TODO implement with frontend panel
    }
}
