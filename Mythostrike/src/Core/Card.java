package Core;

public class Card {
    String name;
    String description;
    CardType type;
    CardData data;
    CardSymbol symbol;
    int point;
    boolean isEquipped;

    public Card(CardData data, CardSymbol symbol, int point){
        name = data.getName();
        description = data.getDescription();
        type = data.getType();
        this.symbol = symbol;
        this.point = point;
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

    public CardData getData() {
        return data;
    }

    public CardSymbol getSymbol() {
        return symbol;
    }

    public int getPoint() {
        return point;
    }

    public boolean isEquipped() {
        return isEquipped;
    }
}
