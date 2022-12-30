package Core;

import java.util.Collections;

public class CardDeck extends CardList{
    private static CardDeck patternDeck;

    public void shuffle(){
        Collections.shuffle(cards);
    }

    public static CardDeck getPatternDeck(){
        return patternDeck;
    }
    public static void setPatternDeck(CardDeck patternDeck){
        CardDeck.patternDeck = patternDeck;
    }
}
