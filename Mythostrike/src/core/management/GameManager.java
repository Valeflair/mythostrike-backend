package core.management;

import core.*;
import core.Player;
import core.activity.Card;
import skill.Skill;
import skill.events.handle.*;
import test.Main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameManager {

    //player has 3 champions to pick in game, godking has 5
    public static final int PICK_CHAMPION_COUNT = 3;
    public static final int PICK_CHAMPION_COUNT_GODKING = 5;
    //player start up with 4 cards and draw 2 cards at each turn start
    public static final int CARD_COUNT_START_UP = 4;
    public static final int CARD_COUNT_TURN_START = 2;



    private final Game game;
    private final CardManager cardManager;
    private final EventManager eventManager;
    private final PlayerManager playerManager;
    private final GameController gameController;

    //debug
    public void debug(String hint){
        System.out.println("D:" + hint);
    }


    public GameManager(ArrayList<Player> players, Mode mode){
        game = new Game(players, mode, this);
        cardManager = new CardManager(this);
        eventManager = new EventManager(this);
        playerManager = new PlayerManager(this);
        gameController = new GameController(this);
    }

    //----------------GameRun management----------------
    public void gameStart(){
        initialGame();
        ArrayList<Player> players = game.getPlayers();

        identityDistribution(players);
        championSelect(players);

        //phase_start = new Event<PhaseHandle>(EventType.PHASE_START);
        //phase_proceeding = new Event<PhaseHandle>(EventType.PHASE_PROCEEDING);

        game.output("Game Started, Player has following champions:");
        for (Player player: players) {
            game.output(player.getName()+ " as Seat " + players.indexOf(player) + " has "+player.getChampion().getName() + "with skill:");
            for (Skill skill : player.getSkills()) {
                game.output(skill.getName() + ":" + skill.getDescription());
                skill.init(eventManager);
            }

            cardManager.drawCard(new CardDrawHandle(this, null, "Draw 4 cards at game start", player, CARD_COUNT_START_UP,game.getDrawDeck()));

        }

        game.setCurrentPlayer(players.get(0));
        game.getCurrentPlayer().setPhase(Phase.ROUNDSTART);
        runPhases();

    }
    public Champion championSelect(Player player, ArrayList<Champion> championList){
        return Main.championSelect(player, championList);
    }
    private void initialGame(){
        game.setDrawDeck(CardDeck.getPatternDeck());
        game.setThrowDeck(new CardDeck());
        game.setTableDeck(new CardDeck());
    }
    private void championSelect(ArrayList<Player> players) {
        List<Champion> championList = Champion.getChampionPatterns();
        for (Player player : players) {

            ArrayList<Champion> list = new ArrayList<>();
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
            player.initialChampions(championSelect(player, list));
            player.setCurrentHp(player.getMaxHp());
        }
    }
    private void identityDistribution(ArrayList<Player> players) {
        Mode mode = game.getMode();
        //only shuffle in identitymode
        if (game.getMode().equals(Mode.IDENTITY_FOR_EIGHT) || game.getMode().equals(Mode.IDENTITY_FOR_FIVE)){
            Collections.shuffle(players);
        }
        //player get its own identity
        for (int i = 0; i < players.size(); i++) {
            players.get(i).setIdentity(mode.getIdentities().get(i));
        }
        //set godking into the first place
        if (!players.get(0).getIdentity().equals(Identity.GODKING) &&
                (mode.equals(Mode.IDENTITY_FOR_FIVE) || mode.equals(Mode.IDENTITY_FOR_EIGHT))){
            for (int i = 0; i < players.size(); i++){
                if (players.get(i).getIdentity().equals(Identity.GODKING)){
                    Player godking = players.get(i);
                    players.set(i, players.get(0));
                    players.set(0,godking);
                }
            }
        }
    }
    public void runPhases(){
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
            case NOTACTIVE -> {
                //go to next player
                Player nextPlayer = game.getPlayers().get((game.getPlayers().indexOf(player) + 1) % game.getPlayers().size());
                game.setCurrentPlayer(nextPlayer);
                changePhase(nextPlayer,Phase.ROUNDSTART,"change phase because he is the next player");
            }
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

                cardManager.drawCard(new CardDrawHandle(this, null, "draw 2 cards at turn start",player, CARD_COUNT_TURN_START, game.getDrawDeck()));

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
                    CardAskHandle cardAskHandle = new CardAskHandle(this, null, "you have to drop " + drop + " Cards because of your HP", player, player.getHandCards(), null, drop, game.getThrowDeck(), false);
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
        for (int i = 0; i < phases.length - 1; i++){
            if (phases[i].equals(phase)){
                changePhase(player, phases[i+1],"proceed to next phase");
            }
        }
        //loop
        runPhases();
    }
    private void askForPlayCard(Player player, CardSpace playableCards) {


        CardAskHandle cardAskHandle = new CardAskHandle(this, null, "Pick a card to play or nothing for end turn", player, playableCards , null, 1, game.getTableDeck(), true);
        if (gameController.askForDiscard(cardAskHandle)) {
            Card card = game.getTableDeck().getCards().get(0);
            CardUseHandle cardUseHandle = new CardUseHandle(this, card, "plays in active", player, new ArrayList<>(), true);
            if (card.getCardData().apply(cardUseHandle)) {
                cleanTable();
                //reduce a number of restrict
                player.getRestrict().put(card.getCardData(), player.getRestrict().get(card.getCardData()) - 1);
            }
            askForPlayCard(game.getCurrentPlayer(), getPlayableCards(game.getCurrentPlayer()));
        }
    }


    private CardSpace getPlayableCards(Player player) {
        CardSpace playableCards = new CardSpace();
        for (Card card : player.getHandCards().getCards()) {
            CardUseHandle cardUseHandle = new CardUseHandle(this, card, "check if card is playable", player, player, true );
            if (card.getCardData().isPlayable(cardUseHandle)) {
                playableCards.addCard(card);
            }
        }
        return playableCards;
    }
    private void changePhase(Player player, Phase phase, String reason){
        PhaseChangeHandle phaseChangeHandle = new PhaseChangeHandle(this, reason, player, player.getPhase(), phase);
        //EventType.PHASE_CHANGING.trigger(phaseChangeHandle);
        player.setPhase(phase);
    }
    private void cleanTable() {
        StringBuilder hint = new StringBuilder("Cards from TableDeck after calculation will get into ThrowDeck :");
        CardDeck throwDeck = game.getThrowDeck();
        CardDeck tableDeck = game.getTableDeck();
        for (Card card : tableDeck.getCards()) {
            hint.append(card.toString()).append(",");

            throwDeck.getCards().add(card);
        }
        tableDeck.getCards().clear();
        debug(hint.toString());
    }

    //---------------getter----------


    public Game getGame() {
        return game;
    }

    public CardManager getCardManager() {
        return cardManager;
    }

    public EventManager getEventManager() {
        return eventManager;
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    public GameController getGameController() {
        return gameController;
    }
}
