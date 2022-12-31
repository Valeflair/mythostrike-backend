package core;

import core.management.GameManager;
import events.handle.*;

import java.util.ArrayList;
import java.util.function.Function;


public enum CardData {
    ATTACK("Attack", "pick a player, he has to play defend or get 1 damage", CardType.BASICCARD, new Function<events.handle.PlayerHandle, Boolean>() {
        @Override
        public Boolean apply(events.handle.PlayerHandle handle) {
            Player player = handle.getFrom();
            ArrayList<Player> targets = new ArrayList<>();
            for (Player target : handle.getGameManager().getGame().getOtherPlayers(player)){
                if (!target.equals(player) && target.isAlive && Boolean.FALSE.equals(target.getImmunity().get(ATTACK))){
                    targets.add(target);
                }
            }
            return !targets.isEmpty() && player.getRestrict().get(ATTACK) > 0 ;
        }
    }, new Function<CardUseHandle, Boolean>() {
        @Override
        public Boolean apply(CardUseHandle cardUseHandle) {
            Player player = cardUseHandle.getFrom();
            GameManager gameManager = cardUseHandle.getGameManager();
            //add targetAble enemy into targets
            ArrayList<Player> targets = new ArrayList<>();
            for (Player target : gameManager.getGame().getOtherPlayers(player)){
                if (!target.equals(player) && target.isAlive && Boolean.FALSE.equals(target.getImmunity().get(ATTACK))){
                    targets.add(target);
                }
            }
            //ask player to pick a target from Attack
            ArrayList<Player> pickTarget = gameManager.getGameController().askForChosePlayer(player, targets, 1, 1, true, "pick a player to attack");
            if(pickTarget.isEmpty()){
                return false;
            }
            if(!gameManager.getGameController().askForConfirm(player, "Confirm your Attack")){
                return false;
            }
            cardUseHandle.setCardUseConfirmed(true);
            for (Player target : pickTarget) {
                AttackHandle attackHandle = new AttackHandle(gameManager, cardUseHandle.getCard(), "", player, target, null, 0);
                gameManager.getEventManager().getAttackEffected().onEvent(attackHandle);
                if (!attackHandle.isPrevented()){
                    gameManager.getEventManager().getAttackProceed().onEvent(attackHandle);
                    CardAskHandle cardAskHandle = attackHandle.getDefendAskHandle();
                    if(gameManager.getGameController().askForDiscard(cardAskHandle)){
                        attackHandle.setPrevented(true);
                    } else {
                        attackHandle.setPrevented(false);
                        DamageHandle damageHandle = new DamageHandle(cardUseHandle.getGameManager(), cardUseHandle.getCard(), "attack damaged", player, target, 1 + attackHandle.getExtraDamage(), DamageType.NORMAL);
                        attackHandle.setDamageHandle(damageHandle);
                    }
                }
            }
            return true;
        }
    })
    , DEFEND("Defend", "use when you are getting Attack, prevent the damage of Attack", CardType.BASICCARD, Constant.conditionAlwaysFalse, Constant.functionDoNothing)
    ;


    private final String name;
    private final String description;
    private final CardType type;
    private final Function<events.handle.PlayerHandle, Boolean> condition;
    private final Function<CardUseHandle, Boolean> function;

    CardData(String name, String description, CardType type, Function<events.handle.PlayerHandle, Boolean> condition, Function<CardUseHandle, Boolean> function) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.condition = condition;
        this.function = function;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public CardType getType() {
        return type;
    }

    public Function<CardUseHandle,Boolean> getFunction() {
        return function;
    }

    public boolean isPlayable(PlayerHandle playerHandle){

        return condition.apply(playerHandle);
    }

    public boolean apply(CardUseHandle cardUseHandle){
       return function.apply(cardUseHandle);
    }

    /**
     * needed to use a constant in the declaration of enum values.
     */
    private static class Constant{
        public static final Function<PlayerHandle, Boolean> conditionAlwaysTrue = new Function<PlayerHandle, Boolean>() {
            @Override
            public Boolean apply(PlayerHandle player) {
                return true;
            }
        };
        public static final Function<PlayerHandle, Boolean> conditionAlwaysFalse = new Function<PlayerHandle, Boolean>() {
            @Override
            public Boolean apply(PlayerHandle player) {
                return false;
            }
        };
        public static final Function<CardUseHandle, Boolean> functionDoNothing = new Function<CardUseHandle, Boolean>() {
            @Override
            public Boolean apply(CardUseHandle cardUseHandle) {
                if(!cardUseHandle.getGameManager().getGameController().askForConfirm(cardUseHandle.getFrom(), "confirm your card use of " + cardUseHandle.getCard().getName())){
                    return false;
                }
                cardUseHandle.setCardUseConfirmed(true);
                return true;
            }
        };
    }
}
