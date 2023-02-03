package com.mythostrike.model.game.core.management;

import com.mythostrike.model.game.Test.Main;
import com.mythostrike.model.game.core.activity.Activity;
import com.mythostrike.model.game.core.activity.cards.CardPile;
import com.mythostrike.model.game.core.activity.cards.CardSpace;
import com.mythostrike.model.game.core.activity.system.Highlight;
import com.mythostrike.model.game.core.activity.system.NextPhase;
import com.mythostrike.model.game.core.player.Champion;
import com.mythostrike.model.game.core.Game;
import com.mythostrike.model.game.core.player.Identity;
import com.mythostrike.model.game.core.Mode;
import com.mythostrike.model.game.core.Phase;
import com.mythostrike.model.game.core.player.Player;
import com.mythostrike.model.game.core.activity.Card;
import com.mythostrike.model.game.core.activity.events.handle.CardAskHandle;
import com.mythostrike.model.game.core.activity.events.handle.CardDrawHandle;
import com.mythostrike.model.game.core.activity.events.handle.CardUseHandle;
import com.mythostrike.model.game.core.activity.events.handle.PhaseChangeHandle;
import lombok.Getter;
import lombok.Setter;

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
                    game.getDrawDeck()));

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
        if (!players.get(0).getIdentity().equals(Identity.GODKING) &&
            (mode.equals(Mode.IDENTITY))) {
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

        while (true) {
            if (currentActivity.isEmpty()) {
                currentActivity.add(new NextPhase(this));
            }
            if (!currentActivity.get(0).getName().equals(Highlight.NAME)) {
                return;
            }
            runActivity();
        }
    }

    private void runActivity() {
        Activity activity = currentActivity.get(0);
        activity.use();
        currentActivity.remove(activity);
    }

    public void runPhases() {
        Player player = game.getCurrentPlayer();
        Phase phase = player.getPhase();
        //debug
        debug("Player " + player.getName() + " starts his phase " + player.getPhase());


        //activate skill.events
        //phase_start.onEvent(new PhaseHandle(this, null, "change phase", player, phase));
        /*
        EventType.PHASE_START.trigger(new PhaseHandle(this, null, "start of phase", player, phase));
        EventType.PHASE_PROCEEDING.trigger(new PhaseHandle(this, null, "proceed of phase", player, phase));

        //ROUNDSTART,DELAYEDEFFECT,DRAW,ACTIVETURN,DISCARD,FINISH,NOTACTIVE
         */
        switch (phase) {
            case ROUNDSTART -> {
                /**
                 * literally do nothing because it is the phase for player to do something before the delayed effect
                 * happens
                 */
                player.getRestrict().put(CardData.ATTACK, 1);
            }
            case DELAYEDEFFECT -> {
                /**
                 * count if player has delayedEffect
                 */
                //TODO : implement delayed effect! and think how!!!
            }

            case DRAW -> {
                //eventmanager.triggerEvent(PhaseStart, PhaseHandle)
                cardManager.drawCard(
                    new CardDrawHandle(this, null, "draw 2 cards at turn start", player, CARD_COUNT_TURN_START,
                        game.getDrawDeck()));
                //eventmanager.triggerEvent(PhaseStart)

            }
            case ACTIVETURN -> {
                CardSpace handCards = player.getHandCards();
                //add all playable cards into list
                CardSpace playableCards = getPlayableCards(player);

                askForPlayCard(player, playableCards);


                //TODO : display all playable cards and ask player to chose
                //TODO : check CardUse playable
            }
            case DISCARD -> {
                //TODO : event for discardododododo
                if (player.getHandCards().getSum() > player.getCurrentHp()) {
                    int drop = player.getHandCards().getSum() - player.getCurrentHp();
                    CardAskHandle cardAskHandle =
                        new CardAskHandle(this, null, "you have to drop " + drop + " Cards because of your HP", player,
                            player.getHandCards(), null, drop, game.getThrowDeck(), false);
                    gameController.askForDiscard(cardAskHandle);
                }
            }
            case FINISH -> {
                /**
                 * finish do literally nothing except for skill invoke
                 */
                //TODO : event for finish

            }
        }

        //EventType.PHASE_END.trigger(new PhaseHandle(this, null, "end of phase", player, phase));

        //go to next phase except NONACTIVE
        Phase[] phases = Phase.values();
        for (int i = 0; i < phases.length - 1; i++) {
            if (phases[i].equals(phase)) {
                changePhase(player, phases[i + 1], "proceed to next phase");
            }
        }
        //loop
        runPhases();
    }

    private void askForPlayCard(Player player, CardSpace playableCards) {

        //TODO:implement with APIs
    }

    private CardSpace getPlayableCards(Player player) {
        CardSpace playableCards = new CardSpace();
        for (Card card : player.getHandCards().getCards()) {
            CardUseHandle cardUseHandle = new CardUseHandle(
                this, card, "check if card is playable", player, player,
                    true);
            if (card.isPlayable(cardUseHandle)) {
                playableCards.add(card);
            }
        }
        return playableCards;
    }

    private void changePhase(Player player, Phase phase, String reason) {
        PhaseChangeHandle phaseChangeHandle = new PhaseChangeHandle(this, reason, player, player.getPhase(), phase);
        //EventType.PHASE_CHANGING.trigger(phaseChangeHandle);
        player.setPhase(phase);
    }

    private void cleanTable() {
        StringBuilder hint = new StringBuilder("Cards from TableDeck after calculation will get into ThrowDeck :");
        CardPile throwDeck = game.getThrowDeck();
        CardPile tableDeck = game.getTableDeck();
        for (Card card : tableDeck.getCards()) {
            hint.append(card.toString()).append(",");

            throwDeck.getCards().add(card);
        }
        tableDeck.getCards().clear();
        debug(hint.toString());
    }

    //---------------getter----------



}
