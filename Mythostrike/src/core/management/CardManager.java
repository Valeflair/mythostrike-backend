package core.management;

import core.*;
import events.handle.CardDrawHandle;
import events.handle.CardMoveHandle;

import java.util.ArrayList;

public class CardManager {
    GameManager gameManager;

    public CardManager(GameManager gameManager){
        this.gameManager = gameManager;
    }


    public Card cloneCard(Card card){
        return new Card (card.getCardData(),card.getSymbol(),card.getPoint());
    }
    //TODO : discuss if its necessary
    public Card cloneCard(CardData data, CardSymbol symbol, int point){
        return new Card (data, symbol, point);
    }
    public void swapDeck(){
        CardDeck dummy = gameManager.getGame().getDrawDeck();
        gameManager.getGame().setDrawDeck(gameManager.getGame().getThrowDeck());
        gameManager.getGame().setThrowDeck(dummy);
        gameManager.getGame().getDrawDeck().shuffle();
    }
    public Card judge(){
        //TODO:use judgeHandle instead judge
        return gameManager.getGame().getDrawDeck().subtractCard();
    }

    public void drawCard(CardDrawHandle cardDrawHandle){

        Player player = cardDrawHandle.getFrom();
        int count = cardDrawHandle.getCount();
        CardDeck drawDeck = cardDrawHandle.getDrawDeck();
        //TODO : add CardDrawEvent
        StringBuilder debug = new StringBuilder("Player " + player.getName() + " draws " + count + "card(s) because of " + cardDrawHandle.getReason() + ", they are :");
        for (int i = 0; i < cardDrawHandle.getCount(); i++){
            Card card = drawDeck.subtractCard();
            player.getHandCards().addCard(card);
            debug.append(card.getName()).append(",");
        }
        debug.delete(debug.length() - 1,debug.length() - 1);
        gameManager.debug(debug.toString());
    }
    public void throwCard(Player player, ArrayList<Card> cards, CardDeck throwDeck, String reason){
        //TODO : call event for CardMoveEvent
        StringBuilder hint = new StringBuilder("Player " + player + " has dropped ");
        for (Card card : cards){
            hint.append(card.getName()).append("(").append(card.getSymbol()).append(" ").append(card.getPoint()).append(")");
            hint.append(",");
        }
        hint.deleteCharAt(hint.length());
        gameManager.getGame().output(hint.toString());
    }
    public void moveCard(CardMoveHandle cardMoveHandle){
        //TODO : update CardMoveEvent
    }
    public void playerGetCardFromList(Player player, CardList cardList, String reason){
        //TODO : implement
    }
}
