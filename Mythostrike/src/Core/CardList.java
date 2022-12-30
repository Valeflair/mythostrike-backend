package Core;

import java.util.ArrayList;
import java.util.Map;

public class CardList {
    protected ArrayList<Card> cards = new ArrayList<Card>();

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
