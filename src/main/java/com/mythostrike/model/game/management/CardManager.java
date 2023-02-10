package com.mythostrike.model.game.management;

import com.mythostrike.model.game.activity.Card;
import com.mythostrike.model.game.activity.cards.CardPile;
import com.mythostrike.model.game.activity.cards.CardSpace;
import com.mythostrike.model.game.activity.events.handle.CardDrawHandle;
import com.mythostrike.model.game.activity.events.handle.CardMoveHandle;
import com.mythostrike.model.game.player.Player;

import java.util.List;

public class CardManager {
    GameManager gameManager;

    public CardManager(GameManager gameManager) {
        this.gameManager = gameManager;
    }


    public void shuffleDiscardPile() {
        CardPile dummy = gameManager.getGame().getDrawPile();
        gameManager.getGame().setDrawPile(gameManager.getGame().getThrowPile());
        gameManager.getGame().setThrowPile(dummy);
        gameManager.getGame().getDrawPile().shuffle();
        //TODO:
        /*CardMoveMessage
        gameManager.getGameController().updateGame(gameManager.getLobbyId(),);*/
        //TODO update it to frontend
    }

    public Card judge() {
        //TODO:use judgeHandle instead judge
        return gameManager.getGame().getDrawPile().subtractCard(0);
    }

    public void drawCard(CardDrawHandle cardDrawHandle) {

        Player player = cardDrawHandle.getPlayer();
        int count = cardDrawHandle.getCount();
        CardPile drawDeck = cardDrawHandle.getDrawPile();
        //TODO : add CardDrawEvent
        StringBuilder debug = new StringBuilder(
            "Player " + player.getUsername() + " draws " + count + "card(s) because of " + cardDrawHandle.getReason()
                + ", they are :");
        for (int i = 0; i < cardDrawHandle.getCount(); i++) {
            Card card = drawDeck.subtractCard(0);
            player.getHandCards().add(card);
            debug.append(card.getName()).append(",");
        }
        debug.delete(debug.length() - 1, debug.length() - 1);
        gameManager.debug(debug.toString());
    }

    public void throwCard(Player player, List<Card> cards, CardSpace fromSpace) {

        CardMoveHandle cardMoveHandle = new CardMoveHandle(gameManager,
            "player drops card", player, null, fromSpace, gameManager.getGame().getThrowPile());
        moveCard(cardMoveHandle);

    }

    public void moveCard(CardMoveHandle cardMoveHandle) {
        List<Card> cards = cardMoveHandle.getMoveCards();
        CardSpace from = cardMoveHandle.getFromSpace();
        CardSpace to = cardMoveHandle.getToSpace();
        from.getCards().removeAll(cards);
        to.getCards().addAll(cards);

        ///TODO update it to frontend
    }

}
