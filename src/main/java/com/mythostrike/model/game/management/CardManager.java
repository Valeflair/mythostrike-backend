package com.mythostrike.model.game.management;

import com.mythostrike.controller.message.game.CardMoveMessage;
import com.mythostrike.model.game.activity.Card;
import com.mythostrike.model.game.activity.cards.CardPile;
import com.mythostrike.model.game.activity.cards.CardSpace;
import com.mythostrike.model.game.activity.events.handle.CardDrawHandle;
import com.mythostrike.model.game.activity.events.handle.CardMoveHandle;
import com.mythostrike.model.game.activity.events.type.EventTypeCardDraw;
import com.mythostrike.model.game.player.Player;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;

public class CardManager {
    private final GameManager gameManager;

    public CardManager(GameManager gameManager) {
        this.gameManager = gameManager;
    }


    public void shuffleDiscardPile() {
        CardPile dummy = gameManager.getGame().getDrawPile();
        gameManager.getGame().setDrawPile(gameManager.getGame().getThrowPile());
        gameManager.getGame().setThrowPile(dummy);
        gameManager.getGame().getDrawPile().shuffle();
        gameManager.output("Discard pile is shuffled");
        /*CardMoveMessage
        gameManager.getGameController().updateGame(gameManager.getLobbyId(),);*/
        //TODO update it to frontend
    }

    public Card judge() {
        //TODO:use judgeHandle instead judge
        Card judge = gameManager.getGame().getDrawPile().subtractCard(0);
        moveCard(new CardMoveHandle(gameManager, "judge", null, null, gameManager.getGame().getDrawPile(),
            gameManager.getGame().getThrowPile(), List.of(judge)));
        //TODO:think if sleep for judge is important so that player can see the card well before it get into discard pile
        try {
            sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return judge;
    }

    public void drawCard(CardDrawHandle cardDrawHandle) {
        gameManager.getEventManager().triggerEvent(EventTypeCardDraw.DRAW_CARD, cardDrawHandle);
        Player player = cardDrawHandle.getPlayer();
        int count = cardDrawHandle.getCount();
        CardPile drawDeck = cardDrawHandle.getDrawPile();
        List<Card> drawenCards = new ArrayList<>();

        StringBuilder message = new StringBuilder(String.format("Player %s draws %d card(s) because of %s, they are :",
            player.getUsername(), count, cardDrawHandle.getReason()));


        for (int i = 0; i < cardDrawHandle.getCount(); i++) {
            Card card = drawDeck.subtractCard(0);
            player.getHandCards().add(card);
            drawenCards.add(card);
            message.append(card.getName()).append(",");
        }

        //delete the last comma
        message.delete(message.length() - 1, message.length() - 1);

        gameManager.debug(message.toString());

        CardMoveHandle cardMoveHandle = new CardMoveHandle(gameManager,
            "player draws cards", player, null, drawDeck, player.getHandCards(), drawenCards);
        moveCard(cardMoveHandle);
    }

    public void throwCard(Player player, List<Card> cards, CardSpace fromSpace) {

        CardMoveHandle cardMoveHandle = new CardMoveHandle(gameManager,
            "player drops card", player, null, fromSpace, gameManager.getGame().getThrowPile(), cards);
        moveCard(cardMoveHandle);
    }

    public void moveCard(CardMoveHandle cardMoveHandle) {
        List<Card> cards = cardMoveHandle.getCardsToMove();
        CardSpace from = cardMoveHandle.getFromSpace();
        CardSpace to = cardMoveHandle.getToSpace();
        from.getCards().removeAll(cards);
        to.getCards().addAll(cards);

        String fromString = "Error";
        CardSpace space = cardMoveHandle.getFromSpace();
        if (cardMoveHandle.getPlayer() == null) {

            if (space.equals(gameManager.getGame().getDrawPile())) {
                fromString = "drawPile";
            }
            if (space.equals(gameManager.getGame().getTablePile())) {
                fromString = "tablePile";
            }
            if (space.equals(gameManager.getGame().getThrowPile())) {
                fromString = "discardPile";
            }
        } else {
            Player player = cardMoveHandle.getPlayer();
            if (space.equals(player.getHandCards())) {
                fromString = player.getUsername();
            }
            if (space.equals(player.getEquipment())) {
                fromString = "equipment-" + player.getUsername();
            }
            if (space.equals(player.getDelayedEffect())) {
                fromString = "delayEffect-" + player.getUsername();
            }
        }
        String toString = "Error";
        space = cardMoveHandle.getToSpace();
        if (cardMoveHandle.getTo() == null) {

            if (space.equals(gameManager.getGame().getDrawPile())) {
                toString = "drawPile";
            }
            if (space.equals(gameManager.getGame().getTablePile())) {
                toString = "tablePile";
            }
            if (space.equals(gameManager.getGame().getThrowPile())) {
                toString = "discardPile";
            }
        } else {
            Player player = cardMoveHandle.getTo();
            if (space.equals(player.getHandCards())) {
                toString = player.getUsername();
            }
            if (space.equals(player.getEquipment())) {
                toString = "equipment-" + player.getUsername();
            }
            if (space.equals(player.getDelayedEffect())) {
                toString = "delayEffect-" + player.getUsername();
            }
        }
        CardMoveMessage cardMoveMessage
            = new CardMoveMessage(fromString, toString, cardMoveHandle.getCardsToMove().size(),
                GameManager.convertCardsToInteger(cardMoveHandle.getCardsToMove()));

        //send private message
        List<String> affectedPlayers = new ArrayList<>();
        if (cardMoveHandle.getPlayer() != null) {
            affectedPlayers.add(cardMoveHandle.getPlayer().getUsername());
        }
        if (cardMoveHandle.getTo() != null) {
            affectedPlayers.add(cardMoveHandle.getTo().getUsername());
        }
        gameManager.getGameController().cardMove(gameManager.getLobbyId(), affectedPlayers, cardMoveMessage);


        //send public message
        cardMoveMessage.cardsId().clear();
        gameManager.getGameController().cardMove(gameManager.getLobbyId(), cardMoveMessage);
    }

}
