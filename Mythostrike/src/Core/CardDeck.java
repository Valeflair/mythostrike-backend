package Core;

import java.util.ArrayList;
import java.util.Collections;

public class CardDeck{
    ArrayList<Card> deck;


    public void addCard(Card card){
        deck.add(card);
    }
    public void addCardToBottom(Card card){
        deck.add(deck.size() - 1,card);
    }
    public Card subtractCard(){
        Card card = deck.get(0);
        deck.remove(card);
        return card;
    }
    public Card subtractCardFromBottom(){
        Card card = deck.get(deck.size() - 1);
        deck.remove(card);
        return card;
    }
    public int getSum(){
        return deck.size();
    }
    public void shuffle(){
        Collections.shuffle(deck);
    }
}
