package Core;

import java.util.function.Function;

public enum CardData {
    ATTACK("Attack","use Attack",CardType.BASICCARD,new Function<Integer, Boolean>(){
        public Boolean apply(Integer integer) {
            return false;
        }
    });

    private String name;
    private String description;
    private CardType type;
    private Function function;

   <T, R> CardData(String name, String description, CardType type, Function<T, R> function) {
        this.name = name;
        this.description = description;
        this.type = type;
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

    public Function getFunction() {
        return function;
    }
}