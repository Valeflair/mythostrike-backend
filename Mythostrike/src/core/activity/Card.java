package core;

public class Card {
    String name;
    String description;
    CardType type;
    CardData cardData;
    CardSymbol symbol;
    int point;
    boolean isEquipped;

    public Card(CardData data, CardSymbol symbol, int point){
        name = data.getName();
        description = data.getDescription();
        type = data.getType();
        this.symbol = symbol;
        this.point = point;
        this.cardData = data;
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

    public CardData getCardData() {
        return cardData;
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

    public boolean isSame(Card card) {
        return card.getCardData().equals(this.cardData);
    }

    public boolean isSame(CardData cardData){
        return cardData.equals(this.cardData);
    }

    public boolean isSame(String name){
        return name.equals(this.name);
    }

    public String toString() {
        return name + "(" + symbol.getShort() + point + ")";
    }
}
