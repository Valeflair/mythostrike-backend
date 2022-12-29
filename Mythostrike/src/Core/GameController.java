package Core;

import Events.DamageHandle;
import Events.DamageType;
import Events.Event;
import Events.PhaseChangeHandle;
import Test.Main;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.function.Function;

public class GameController {
    public static final int PICK_CHAMPION_COUNT = 3;
    public static final int PICK_CHAMPION_COUNT_GODKING = 5;
    private Game game;

    //debug
    public void debug(String hint){
        System.out.println("D:" + hint);
    }


    public GameController(Game game){
        this.game = game;
    }
    //----------------GameRun management----------------
    public void gameStart(){
        ArrayList<Player> players = game.getPlayers();

        identityDistribution(players);
        championSelect(players);

        game.output("Game Started, Player has following champions:");
        for(Player player: players) {
            game.output(player.getName()+" as Seat " + players.indexOf(player) + " has "+player.getChampion().getName() + "with skill:");
            for(Skill<?> skill : player.getSkills()){
                game.output(skill.getName() +":"+skill.getDescription());
                Class<?> skillClass = skill.getClass();
                Type type=skillClass.getGenericSuperclass();
                ParameterizedType pType=(ParameterizedType)type;
                Class skillType=(Class) pType.getActualTypeArguments()[0];
                System.out.println(skillType + "compare to" + Skill_Trigger.class);
                if (skillType.equals(Skill_Trigger.class)){
                    Skill_Trigger triggerSkill = (Skill_Trigger) skill;
                    for (Event event : triggerSkill.getEvents()){
                        event.addFunction(skill.getFunction());
                    }
                }
            }
        }

        game.setCurrentPlayer(players.get(0));
        runPhases();

    }
    public Champion championSelect(Player player, ArrayList<Champion> championList){
        return Main.championSelect(player, championList);
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
        if (game.getMode().equals(Mode.IDENTITYFOREIGHT) || game.getMode().equals(Mode.IDENTITYFORFIVE)){
            Collections.shuffle(players);
        }
        //player get its own identity
        for (int i = 0; i < players.size(); i++) {
            players.get(i).setIdentity(mode.identities.get(i));
        }
        //set godking into the first place
        if (!players.get(0).getIdentity().equals(Identity.GODKING) &&
                (mode.equals(Mode.IDENTITYFORFIVE) || mode.equals(Mode.IDENTITYFOREIGHT))){
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
        debug("Player " + player.getName() + "start his phase" + player.getPhase());


        //activate events

        for ( Function<?,?> function : Event.PHASESTART.getFunctionList()){
            // TODO: ask how to cast it properly
            Function<Phase,?> concretFunction = (Function<Phase,?>) function;
            concretFunction.apply(phase);
        }
        for ( Function<?,?> function : Event.PHASEPROCEEDING.getFunctionList()){
            // TODO: ask how to cast it properly
            Function<Phase,?> concretFunction = (Function<Phase,?>) function;
            concretFunction.apply(phase);
        }
        //TODO : fertig stellen
        //ROUNDSTART,DELAYEDEFFECT,DRAW,ACTIVETURN,DISCARD,FINISH,NOTACTIVE
        switch (phase) {
            case NOTACTIVE -> {
                //go to next player
                Player nextPlayer = game.getPlayers().get(game.getPlayers().indexOf(player) + 1);
                game.setCurrentPlayer(nextPlayer);
                changePhase(nextPlayer,Phase.ROUNDSTART);
            }
            case ROUNDSTART -> {

            }
            case DELAYEDEFFECT -> {

            }

            case DRAW -> {


            }
            case ACTIVETURN -> {


            }
            case DISCARD -> {

            }
            case FINISH -> {

            }
        }

        for ( Function<?,?> function : Event.PHASEEND.getFunctionList()){
            // TODO: ask how to cast it properly
            Function<Phase,?> concretFunction = (Function<Phase,?>) function;
            concretFunction.apply(phase);
        }

        //go to next phase except NONACTIVE
        Phase[] phases = Phase.values();
        for (int i = 0; i < phases.length - 1; i++){
            if (phases[i].equals(phase)){
                changePhase(player, phases[i+1]);
            }
        }
        //loop
        runPhases();
    }
    private void changePhase(Player player, Phase phase){
        PhaseChangeHandle phaseChangeHandle = new PhaseChangeHandle(player, player.getPhase(), phase, this);
        for (Function<?,?> function : Event.PHASECHANGING.getFunctionList()){
            // TODO: ask how to cast it properly
            Function<PhaseChangeHandle,?> concretFunction = (Function<PhaseChangeHandle,?>) function;
            concretFunction.apply(phaseChangeHandle);
        }
    }
    //----------------Card management----------------
    public Card judge(){
        //TODO:use judgeHandle instead judge
        return game.getDrawDeck().subtractCard();
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
    public boolean askForDiscard(Player player, CardSpace targetSpace, int min, int max, boolean optional, String reason){
        ArrayList<Card> cards = askForCard(player, targetSpace, min, max, optional, reason);
        if (cards.isEmpty()){
            return false;
        } else {
            throwCard(player, cards, game.getThrowDeck(),reason);
            return true;
        }
    }
    public ArrayList<Card> askForCard(Player player, CardSpace targetSpace, int min, int max, boolean optional, String reason){
        return Main.askForCard(player,targetSpace,min,max,optional,reason);
    }

}
