package com.mythostrike.model.game.activity.cards;

import com.mythostrike.model.exception.IllegalInputException;
import com.mythostrike.model.game.activity.Card;
import com.mythostrike.model.game.activity.cards.cardtype.Attack;
import com.mythostrike.model.game.activity.cards.cardtype.Defend;
import com.mythostrike.model.game.activity.cards.cardtype.Heal;
import com.mythostrike.model.game.activity.cards.cardtype.Nightmare;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class CardList {

    private static CardList instance;
    private final Map<Integer, Card> cards;

    /**
     * This is a Singleton Class. The Constructor is private.
     * Use the getModeList() method to get the instance.
     * The ModeList is created from the Enum Values ModeData.
     */
    @SuppressWarnings("ValueOfIncrementOrDecrementUsed")
    private CardList() {
        this.cards = new HashMap<>();

        //initilize a complete card deck in the card list
        int id = 1000;
        //TODO: doppelte entfernen bzw. neu verteilen
        //stand von ausgedruckten Karten
        /*cards.put(++id, new GoldenApple(id, CardSymbol.HEART, 1));
        cards.put(++id, new GoldenApple(id, CardSymbol.HEART, 3));
        cards.put(++id, new GoldenApple(id, CardSymbol.HEART, 4));*/
        /*cards.put(++id, new GodArena(id, CardSymbol.CLUB, 7));
        cards.put(++id, new GodArena(id, CardSymbol.CLUB, 13));
        cards.put(++id, new GodArena(id, CardSymbol.CLUB, 12));
        cards.put(++id, new GodArena(id, CardSymbol.SPADE, 7));
        cards.put(++id, new GodArena(id, CardSymbol.SPADE, 13));*/
        cards.put(++id, new Attack(id, CardSymbol.HEART, 10));
        cards.put(++id, new Attack(id, CardSymbol.HEART, 10)); //doppelt
        cards.put(++id, new Attack(id, CardSymbol.HEART, 12));
        cards.put(++id, new Attack(id, CardSymbol.DIAMOND, 6));
        cards.put(++id, new Attack(id, CardSymbol.DIAMOND, 7));
        cards.put(++id, new Attack(id, CardSymbol.DIAMOND, 8));
        cards.put(++id, new Attack(id, CardSymbol.DIAMOND, 9));
        cards.put(++id, new Attack(id, CardSymbol.DIAMOND, 10));
        cards.put(++id, new Attack(id, CardSymbol.DIAMOND, 13));
        cards.put(++id, new Attack(id, CardSymbol.CLUB, 1));
        cards.put(++id, new Attack(id, CardSymbol.CLUB, 2));
        cards.put(++id, new Attack(id, CardSymbol.CLUB, 3));
        cards.put(++id, new Attack(id, CardSymbol.CLUB, 4));
        cards.put(++id, new Attack(id, CardSymbol.CLUB, 5));
        cards.put(++id, new Attack(id, CardSymbol.CLUB, 6));
        cards.put(++id, new Attack(id, CardSymbol.CLUB, 7));
        cards.put(++id, new Attack(id, CardSymbol.CLUB, 8));
        cards.put(++id, new Attack(id, CardSymbol.CLUB, 8)); //doppelt
        cards.put(++id, new Attack(id, CardSymbol.CLUB, 9));
        cards.put(++id, new Attack(id, CardSymbol.CLUB, 9)); //doppelt
        cards.put(++id, new Attack(id, CardSymbol.CLUB, 10));
        cards.put(++id, new Attack(id, CardSymbol.CLUB, 10)); //doppelt
        cards.put(++id, new Attack(id, CardSymbol.CLUB, 11));
        cards.put(++id, new Attack(id, CardSymbol.CLUB, 11)); //doppelt
        cards.put(++id, new Attack(id, CardSymbol.SPADE, 7));
        cards.put(++id, new Attack(id, CardSymbol.SPADE, 8));
        cards.put(++id, new Attack(id, CardSymbol.SPADE, 8)); //doppelt
        cards.put(++id, new Attack(id, CardSymbol.SPADE, 9));
        cards.put(++id, new Attack(id, CardSymbol.SPADE, 9)); //doppelt
        cards.put(++id, new Attack(id, CardSymbol.SPADE, 10));
        cards.put(++id, new Attack(id, CardSymbol.SPADE, 10)); //doppelt
        /*cards.put(++id, new BlessOfHecate(id, CardSymbol.HEART, 7));
        cards.put(++id, new BlessOfHecate(id, CardSymbol.HEART, 8));
        cards.put(++id, new BlessOfHecate(id, CardSymbol.HEART, 9));
        cards.put(++id, new BlessOfHecate(id, CardSymbol.HEART, 11));*/
        cards.put(++id, new Defend(id, CardSymbol.HEART, 2));
        cards.put(++id, new Defend(id, CardSymbol.HEART, 2)); //doppelt
        cards.put(++id, new Defend(id, CardSymbol.HEART, 13));
        cards.put(++id, new Defend(id, CardSymbol.DIAMOND, 2));
        cards.put(++id, new Defend(id, CardSymbol.DIAMOND, 2)); //doppelt
        cards.put(++id, new Defend(id, CardSymbol.DIAMOND, 3));
        cards.put(++id, new Defend(id, CardSymbol.DIAMOND, 3)); //doppelt
        cards.put(++id, new Defend(id, CardSymbol.DIAMOND, 5));
        cards.put(++id, new Defend(id, CardSymbol.DIAMOND, 6));
        cards.put(++id, new Defend(id, CardSymbol.DIAMOND, 7));
        cards.put(++id, new Defend(id, CardSymbol.DIAMOND, 8));
        cards.put(++id, new Defend(id, CardSymbol.DIAMOND, 9));
        cards.put(++id, new Defend(id, CardSymbol.DIAMOND, 10));
        cards.put(++id, new Defend(id, CardSymbol.DIAMOND, 11));
        cards.put(++id, new Defend(id, CardSymbol.DIAMOND, 11)); //doppelt
        /*cards.put(++id, new Duel(id, CardSymbol.HEART, 3));
        cards.put(++id, new Duel(id, CardSymbol.HEART, 4));
        cards.put(++id, new Duel(id, CardSymbol.DIAMOND, 1));
        cards.put(++id, new Duel(id, CardSymbol.CLUB, 1));
        cards.put(++id, new Duel(id, CardSymbol.SPADE, 1));*/
        /*cards.put(++id, new Extort(id, CardSymbol.DIAMOND, 3));
        cards.put(++id, new Extort(id, CardSymbol.DIAMOND, 4));
        cards.put(++id, new Extort(id, CardSymbol.SPADE, 3));
        cards.put(++id, new Extort(id, CardSymbol.SPADE, 4));
        cards.put(++id, new Extort(id, CardSymbol.SPADE, 11));*/
        cards.put(++id, new Heal(id, CardSymbol.HEART, 3));
        cards.put(++id, new Heal(id, CardSymbol.HEART, 4));
        cards.put(++id, new Heal(id, CardSymbol.HEART, 6));
        cards.put(++id, new Heal(id, CardSymbol.HEART, 7));
        cards.put(++id, new Heal(id, CardSymbol.HEART, 8));
        cards.put(++id, new Heal(id, CardSymbol.HEART, 9));
        cards.put(++id, new Heal(id, CardSymbol.HEART, 12));
        cards.put(++id, new Heal(id, CardSymbol.DIAMOND, 12));
        cards.put(++id, new Nightmare(id, CardSymbol.HEART, 6));
        cards.put(++id, new Nightmare(id, CardSymbol.CLUB, 6));
        cards.put(++id, new Nightmare(id, CardSymbol.SPADE, 6));
        /*cards.put(++id, new VolcanicEruption(id, CardSymbol.DIAMOND, 12));
        cards.put(++id, new VolcanicEruption(id, CardSymbol.HEART, 13));
        cards.put(++id, new VolcanicEruption(id, CardSymbol.CLUB, 13));
        cards.put(++id, new VolcanicEruption(id, CardSymbol.CLUB, 12));
        cards.put(++id, new VolcanicEruption(id, CardSymbol.SPADE, 11));*/
    }

    public static CardList getCardList() {
        if (instance == null) {
            instance = new CardList();
        }
        return instance;
    }

    public @NotNull @UnmodifiableView List<Card> getCards() {
        return List.copyOf(cards.values());
    }

    public List<Card> getFullCardDeck() {
        return getCards().stream().map(Card::deepCopy).toList();
    }

    public Card getCard(int id) throws IllegalInputException {
        Card card = cards.get(id);
        if (card == null) {
            throw new IllegalInputException(String.format("The id %d is not valid.", id));
        }
        return card;
    }
}
