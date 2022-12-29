package Core;

import java.util.ArrayList;

public class CardSpace {
    ArrayList<Card> cards;

    public CardSpace(){
        cards = new ArrayList<Card>();
    }

    public void addCard(Card card){
        cards.add(card);
    }
    public void subtractCard(Card card){
        cards.remove(card);
    }
    public Card subtractCard(int index){
        Card card = cards.get(index);
        cards.remove(card);
        return card;
    }
    public int getSum(){
        return cards.size();
    }
}
