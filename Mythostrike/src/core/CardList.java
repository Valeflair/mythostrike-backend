package core;

import java.util.ArrayList;

public class CardList{
    protected ArrayList<Card> cards;

    public CardList(){cards = new ArrayList<>();}
    public CardList(ArrayList<Card> cards){
        cards = new ArrayList<>(cards);
    }

    public void addCard(Card card){
        cards.add(card);
    }
    public void addCardToBottom(Card card){
        cards.add(cards.size() - 1,card);
    }
    public Card subtractCard(){
        Card card = cards.get(0);
        cards.remove(card);
        return card;
    }
    public Card subtractCardFromBottom(){
        Card card = cards.get(cards.size() - 1);
        cards.remove(card);
        return card;
    }
    public int getSum(){
        return cards.size();
    }

    public ArrayList<Card> getCards(){
        return cards;
    }
}
