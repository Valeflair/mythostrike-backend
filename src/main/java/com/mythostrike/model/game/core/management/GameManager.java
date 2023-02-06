package com.mythostrike.model.game.core.management;

import com.mythostrike.model.game.Test.Main;
import com.mythostrike.model.game.core.activity.Activity;
import com.mythostrike.model.game.core.activity.cards.CardPile;
import com.mythostrike.model.game.core.activity.system.NextPhase;
import com.mythostrike.model.game.core.activity.system.PickRequest;
import com.mythostrike.model.game.core.player.Champion;
import com.mythostrike.model.game.core.Game;
import com.mythostrike.model.game.core.player.Identity;
import com.mythostrike.model.game.core.Mode;
import com.mythostrike.model.game.core.Phase;
import com.mythostrike.model.game.core.player.Player;
import com.mythostrike.model.game.core.activity.Card;
import com.mythostrike.model.game.core.activity.events.handle.CardDrawHandle;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

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
    private List<Activity> currentActivity;
    @Getter
    @Setter
    private Phase phase;

    @Getter
    private final Game game;
    @Getter
    private final CardManager cardManager;
    @Getter
    private final EventManager eventManager;
    @Getter
    private final PlayerManager playerManager;
    @Getter
    private final GameController gameController;

    private boolean proceeding;




    public GameManager(ArrayList<Player> players, Mode mode, GameController gameController) {
        game = new Game(players, mode, this);
        cardManager = new CardManager(this);
        eventManager = new EventManager(this);
        playerManager = new PlayerManager(this);
        this.gameController = gameController;
    }

    //debug
    public void debug(String hint) {
        System.out.println("D:" + hint);
    }

    //----------------GameStart----------------
    public void gameStart() {

        ArrayList<Player> players = game.getAlivePlayers();
        //TODO:unterschiedliche dinge an frontend schicken
        identityDistribution(players);
        //TODO:extra panel machen
        selectChampionPhase(players);



        game.output("Game Started, Player has following champions:");
        for (Player player : players) {
            game.output(
                player.getName() + " as Seat " + players.indexOf(player) + " has " + player.getChampion().getName()
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

    public Champion playerPickChampionFromList(Player player, List<Champion> championList) {
        //TODO adjust with API
        return Main.championSelect(player, championList);
    }

    private void identityDistribution(ArrayList<Player> players) {
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

    //----------------GameRun----------------

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
        currentActivity.remove(activity);
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

    //---------------getter----------



}
