package Core;

import Events.*;
import Events.Handle.*;
import Test.Main;

import java.util.ArrayList;
import java.util.Collections;

public class GameController {

    //player has 3 champions to pick in game, godking has 5
    public static final int PICK_CHAMPION_COUNT = 3;
    public static final int PICK_CHAMPION_COUNT_GODKING = 5;
    //player start up with 4 cards and draw 2 cards at each turn start
    public static final int CARD_COUNT_START_UP = 4
            ;
    public static final int CARD_COUNT_TURN_START = 2;

    private Event<PhaseHandle> phaseStart;
    private Event<PhaseHandle> phaseProceeding;
    private Event<DamageHandle> damaged;

    private Game game;

    //debug
    public void debug(String hint){
        System.out.println("D:" + hint);
    }


    public GameController(Game game){
        this.game = game;
    }

    public Game getGame() {
        return game;
    }

    //----------------GameRun management----------------
    public void gameStart(){
        ArrayList<Player> players = game.getPlayers();

        identityDistribution(players);
        championSelect(players);

        //phase_start = new Event<PhaseHandle>(EventType.PHASE_START);
        //phase_proceeding = new Event<PhaseHandle>(EventType.PHASE_PROCEEDING);

        game.output("Game Started, Player has following champions:");
        for(Player player: players) {
            game.output(player.getName()+" as Seat " + players.indexOf(player) + " has "+player.getChampion().getName() + "with skill:");
            for(Skill skill : player.getSkills()){
                game.output(skill.getName() +":"+skill.getDescription());

                //@TODO : fix it with EventListener

            }

            drawCard(new CardDrawHandle(this, null, "Draw 4 cards at game start", player, CARD_COUNT_START_UP,game.getDrawDeck()));

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
        ArrayList<Champion> championList = Champion.getChampionPatterns();
        for (Player player : players) {

            ArrayList<Champion> list = new ArrayList<Champion>();
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
            players.get(i).setIdentity(mode.identities.get(i));
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


        //activate events
        //phase_start.onEvent(new PhaseHandle(this, null, "change phase", player, phase));
        /*
        EventType.PHASE_START.trigger(new PhaseHandle(this, null, "start of phase", player, phase));
        EventType.PHASE_PROCEEDING.trigger(new PhaseHandle(this, null, "proceed of phase", player, phase));

        //ROUNDSTART,DELAYEDEFFECT,DRAW,ACTIVETURN,DISCARD,FINISH,NOTACTIVE
         */
        switch (phase) {
            case NOTACTIVE -> {
                //go to next player
                Player nextPlayer = game.getPlayers().get(game.getPlayers().indexOf(player) + 1);
                game.setCurrentPlayer(nextPlayer);
                changePhase(nextPlayer,Phase.ROUNDSTART,"change phase because he is the next player");
            }
            case ROUNDSTART -> {
                /**
                 * literally do nothing because it is the phase for player to do something before the delayed effect
                 * happens
                 */

            }
            case DELAYEDEFFECT -> {
                /**
                 * count if player has delayedEffect
                 */
                //TODO : implement delayed effect! and think how!!!
            }

            case DRAW -> {

                drawCard(new CardDrawHandle(this,null,"draw 2 cards at turn start",player, CARD_COUNT_TURN_START, game.getDrawDeck()));

            }
            case ACTIVETURN -> {
                //TODO : display all playable cards and ask player to chose
                //TODO : check CardUse playable
            }
            case DISCARD -> {
                //TODO : event for discardododododo
                if (player.getHandCards().getSum() > player.getCurrentHp()){
                    int drop = player.getHandCards().getSum() - player.getCurrentHp();
                    askForDiscard(player, game.getThrowDeck(), drop, drop, false, "you have to drop " + drop + " Cards because of your HP");
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
    private void changePhase(Player player, Phase phase, String reason){
        PhaseChangeHandle phaseChangeHandle = new PhaseChangeHandle(this, null, reason, player, player.getPhase(), phase);
        //EventType.PHASE_CHANGING.trigger(phaseChangeHandle);
        player.setPhase(phase);
    }
    //----------------Card management----------------

    public Card cloneCard(Card card){
        return new Card (card.getData(),card.getSymbol(),card.getPoint());
    }
    //TODO : discuss if its necessary
    public Card cloneCard(CardData data, CardSymbol symbol, int point){
        return new Card (data, symbol, point);
    }

    public void swapDeck(){
        CardDeck dummy = game.getDrawDeck();
        game.setDrawDeck(game.getThrowDeck());
        game.setThrowDeck(dummy);
        game.getDrawDeck().shuffle();
    }


    public Card judge(){
        //TODO:use judgeHandle instead judge
        return game.getDrawDeck().subtractCard();
    }

    public void drawCard(CardDrawHandle cardDrawHandle){

        Player player = cardDrawHandle.getFrom();
        int count = cardDrawHandle.getCount();
        CardDeck drawDeck = cardDrawHandle.getDrawDeck();
        //TODO : add CardDrawEvent
        StringBuilder debug = new StringBuilder("Player " + player.getName() + " draws " + count + "card(s) because of " + cardDrawHandle.getReason() + ", they are :");
        for (int i = 0; i < cardDrawHandle.getCount(); i++){
            Card card = drawDeck.subtractCard();
            player.getHandCards().addCard(card);
            debug.append(card.getName()).append(",");
        }
        debug.delete(debug.length() - 1,debug.length() - 1);
        debug(debug.toString());
    }
    public void throwCard(Player player, ArrayList<Card> cards, CardDeck throwDeck, String reason){
        //TODO : call event for CardMoveEvent
        StringBuilder hint = new StringBuilder("Player " + player + " has dropped ");
        for (Card card : cards){
            hint.append(card.getName()).append("(").append(card.getSymbol()).append(" ").append(card.getPoint()).append(")");
            hint.append(",");
        }
        hint.deleteCharAt(hint.length());
        game.output(hint.toString());
    }
    public void moveCard(CardMoveHandle cardMoveHandle){
        //TODO : update CardMoveEvent
    }
    public void playerGetCardFromList(Player player, CardList cardList, String reason){

    }


    //----------------Player management----------------

    public void applyDamage(DamageHandle damageHandle){
        //TODO : call event for damage, positive damage for damage event, negative damage for heal event
        Player from = damageHandle.getFrom();
        Player to = damageHandle.getTo();
        int damage = damageHandle.getDamage();
        DamageType type = damageHandle.getDamageType();

        if(!damageHandle.isPrevented()){

            //reduce hp
            to.setCurrentHp(to.getCurrentHp() - damage);

            //output message
            String hint = "Player " + from.getName();
            hint += " deals " + damage + " ";
            if(!type.equals(DamageType.NORMAL)){
                hint += type.toString();
            }
            hint += " damage to Player" + to.getName();
            hint += ", ouch! And he has now " + to.getCurrentHp() + " HP.";
            game.output(hint);
        }

    }

    //----------------AskFor management----------------

    public boolean askForSkillInvoke(Player player, Skill skill){
        String hint = "do you want to active Skill" + skill.getName() + "?";
        return Main.askForConfirm(player, hint);
    }
    public boolean askForDiscard(Player player, CardList targetSpace, int min, int max, boolean optional, String reason){
        ArrayList<Card> cards = askForCard(player, targetSpace, min, max, optional, reason);
        if (cards.isEmpty()){
            return false;
        } else {
            throwCard(player, cards, game.getThrowDeck(),reason);
            return true;
        }
    }
    public ArrayList<Card> askForCard(Player player, CardList targetSpace, int min, int max, boolean optional, String reason){
        return Main.askForCard(player,targetSpace,min,max,optional,reason);
    }
    public ArrayList<Player> askForChosePlayer(Player player, ArrayList<Player> targetPlayers, int min, int max, boolean optional, String reason){
        return Main.askForChosePlayer( player, targetPlayers, min, max, optional, reason);
    }



}
