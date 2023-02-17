package com.mythostrike.model.game.management;

import com.mythostrike.controller.message.game.CardMoveMessage;
import com.mythostrike.model.game.Game;
import com.mythostrike.model.game.activity.cards.*;
import com.mythostrike.model.game.activity.events.handle.CardDrawHandle;
import com.mythostrike.model.game.activity.events.handle.CardFilterHandle;
import com.mythostrike.model.game.activity.events.handle.CardMoveHandle;
import com.mythostrike.model.game.activity.events.type.EventTypeCardDraw;
import com.mythostrike.model.game.activity.events.type.EventTypeCardMove;
import com.mythostrike.model.game.activity.events.type.EventTypeFilter;
import com.mythostrike.model.game.player.Player;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.lang.Thread.sleep;

public class CardManager {

    private static final Set<CardSpaceType> PRIVAT_CARD_SPACES
        = new HashSet<>(Set.of(CardSpaceType.HAND_CARDS, CardSpaceType.DRAW_PILE));

    private final GameManager gameManager;

    public CardManager(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    /**
     * move all cards from discard pile to draw pile. Shuffle the draw pile.
     */
    public void refillDrawPile() {
        CardMoveHandle cardMoveHandle = new CardMoveHandle(
            gameManager, "refill draw pile", null, null, gameManager.getGame().getDiscardPile(),
            gameManager.getGame().getDrawPile(), gameManager.getGame().getDiscardPile().getCards()
        );

        moveCard(cardMoveHandle);
        gameManager.getGame().getDrawPile().shuffle(Game.RANDOM_SEED);
        gameManager.output("Discard pile is shuffled");
    }

    public Card judge() {
        //TODO:use judgeHandle instead judge
        Card judge = gameManager.getGame().getDrawPile().peekTop();
        moveCard(new CardMoveHandle(gameManager, "judge", null, null, gameManager.getGame().getDrawPile(),
            gameManager.getGame().getDiscardPile(), List.of(judge)));
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


        if (drawDeck.peekTop(count) == null) {
            refillDrawPile();
        }
        List<Card> drawedCards = drawDeck.peekTop(count);

        //create a debug message
        StringBuilder message = new StringBuilder(String.format("Player %s draws %d card(s) because of %s, they are :",
            player.getUsername(), count, cardDrawHandle.getReason()));

        for (Card card : drawedCards) {
            message.append(card).append(",");
        }
        //delete the last comma
        message.delete(message.length() - 1, message.length() - 1);
        gameManager.debug(message.toString());


        CardMoveHandle cardMoveHandle = new CardMoveHandle(gameManager,
            "player draws cards", player, null, drawDeck, player.getHandCards(), drawedCards);
        moveCard(cardMoveHandle);
    }

    public void throwCard(Player player, List<Card> cards, CardSpace fromSpace) {

        CardMoveHandle cardMoveHandle = new CardMoveHandle(gameManager,
            "player drops card", player, null, fromSpace, gameManager.getGame().getDiscardPile(), cards);
        moveCard(cardMoveHandle);
    }

    /**
     * Move cards specified in cardMoveHandle from 'from' space to 'to' space. Also sends a message to the frontend.
     *
     * @param cardMoveHandle the handle that contains the information of the move
     */
    public void moveCard(CardMoveHandle cardMoveHandle) {
        gameManager.getEventManager().triggerEvent(EventTypeCardMove.BEFORE_CARD_MOVE, cardMoveHandle);
        List<Card> cards = cardMoveHandle.getCardsToMove();
        CardSpace from = cardMoveHandle.getFromSpace();
        CardSpace to = cardMoveHandle.getToSpace();

        from.removeAll(cards);
        to.addAll(cards);

        gameManager.getEventManager().triggerEvent(EventTypeCardMove.AFTER_CARD_MOVE, cardMoveHandle);
        CardMoveMessage cardMoveMessage = new CardMoveMessage(
            from.getName(), to.getName(), cardMoveHandle.getCardsToMove().size(),
            GameManager.convertCardsToInteger(cardMoveHandle.getCardsToMove())
        );


        //if both from and to space are private, send the message with the cardIds only to the affected player
        if (from.getType().isConcealed() && to.getType().isConcealed()) {
            //send private message
            List<String> affectedPlayers = new ArrayList<>();
            if (cardMoveHandle.getPlayer() != null) {
                affectedPlayers.add(cardMoveHandle.getPlayer().getUsername());
            }
            if (cardMoveHandle.getTo() != null) {
                affectedPlayers.add(cardMoveHandle.getTo().getUsername());
            }
            gameManager.getGameController().cardMove(gameManager.getLobbyId(), affectedPlayers, cardMoveMessage);

            //clear the cardIds for the public message
            cardMoveMessage.cardIds().clear();
        }

        //send public message
        gameManager.getGameController().cardMove(gameManager.getLobbyId(), cardMoveMessage);
    }

    public List<Card> filterCard(List<Card> cards, CardFilter cardFilter, Player player) {
        CardFilterHandle cardFilterHandle = new CardFilterHandle(gameManager, player, cardFilter, cards);
        gameManager.getEventManager().triggerEvent(EventTypeFilter.CARD_FILTER, cardFilterHandle);

        return cardFilterHandle.getCardFilter().filter(cards);
    }

    public boolean cardMatchFilter(Card card, CardFilter cardFilter, Player player) {
        CardFilterHandle cardFilterHandle = new CardFilterHandle(gameManager, player, cardFilter, List.of(card));
        gameManager.getEventManager().triggerEvent(EventTypeFilter.CARD_FILTER, cardFilterHandle);
        return cardFilter.match(card);
    }

}
