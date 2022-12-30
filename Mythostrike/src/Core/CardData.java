package Core;

import Events.Handle.CardUseHandle;

import java.util.ArrayList;
import java.util.function.Function;


public enum CardData {

    ATTACK("Attack", "pick a player, he has to play defend or get 1 damage", CardType.BASICCARD, Constant.noCondition, new Function<CardUseHandle, Boolean>() {
        @Override
        public Boolean apply(CardUseHandle handle) {
            Player player = handle.getFrom();
            ArrayList<Player> targets = new ArrayList<Player>();
            //add those player not immune to attack into targetable list
            for (Player target : handle.getGameController().getGame().getOtherPlayers(player)){
                if (!target.getImmunity().get(ATTACK)){
                    targets.add(target);
                }
            }
            targets = handle.getGameController().askForChosePlayer(player, targets, 1,1,true,"pick a player to attack");
            if (!targets.isEmpty()){

            }


            return null;
        }
    }
    );


    private final String name;
    private final String description;
    private final CardType type;
    private final Function<Player,Boolean> condition;
    private final Function<CardUseHandle,Boolean> function;

    CardData(String name, String description, CardType type, Function<Player, Boolean> condition, Function<CardUseHandle, Boolean> function) {
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

    public boolean isPlayable(Player player){
       return condition.apply(player) && (player.getRestrict().get(this) <= 0);
    }

    public boolean apply(CardUseHandle cardUseHandle){
       return function.apply(cardUseHandle);
    }

    /**
     * needed to use a constant in the declaration of enum values.
     */
    private static class Constant{
        public static final Function<Player, Boolean> noCondition = new Function<Player, Boolean>() {
            @Override
            public Boolean apply(Player player) {
                return true;
            }
        };
    }
}
