package com.mythostrike.model.game.management;

import com.mythostrike.controller.message.game.CardMoveMessage;
import com.mythostrike.model.game.Game;
import com.mythostrike.model.game.activity.cards.Card;
import com.mythostrike.model.game.activity.cards.CardFilter;
import com.mythostrike.model.game.activity.cards.CardPile;
import com.mythostrike.model.game.activity.cards.CardSpace;
import com.mythostrike.model.game.activity.cards.CardSpaceType;
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
import java.util.stream.Collectors;

import static java.lang.Thread.sleep;

public class CardManager {

    public static final int JUDGE_PAUSE_ON_TABLE_PILE = 1000;
    private static final int CARD_MOVE_PAUSE_BETWEEN_MOVEMENT = 1000;

    private static final Set<CardSpaceType> PRIVAT_CARD_SPACES
        = new HashSet<>(Set.of(CardSpaceType.HAND_CARDS, CardSpaceType.DRAW_PILE));
    private final GameManager gameManager;

    public CardManager(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    /**
     * move all cards from discard pile to draw pile. Shuffle the draw pile.
     */
    private void refillDrawPile() {
        CardMoveHandle cardMoveHandle = new CardMoveHandle(
            gameManager, "refill draw pile", null, null, gameManager.getGame().getDiscardPile(),
            gameManager.getGame().getDrawPile(), gameManager.getGame().getDiscardPile().getCards()
        );

        moveCard(cardMoveHandle);
        gameManager.getGame().getDrawPile().shuffle(Game.RANDOM_SEED);
        gameManager.output("Discard pile is shuffled");
    }

    public List<Card> peekTopDrawPile(int count) {
        List<Card> cards = gameManager.getGame().getDrawPile().peekTop(count);
        if (cards == null) {
            cards = new ArrayList<>(gameManager.getGame().getDrawPile().getCards());
            refillDrawPile();
            List<Card> extraCards = gameManager.getGame().getDrawPile().peekTop(count - cards.size());
            if (extraCards == null) {
                throw new IllegalArgumentException("draw pile is not big enough");
            }
            cards.addAll(extraCards);
        }
        return cards;
    }

    /**
     * draw the top card from draw pile and put it on the table pile,
     * wait 1 second and then move the card to the discard pile.
     *
     * @return the card that is drawn
     */
    public Card judge() {
        //TODO:use judgeHandle instead judge
        Card judge = peekTopDrawPile(1).get(0);
        moveCard(new CardMoveHandle(gameManager, "judge", null, null, gameManager.getGame().getDrawPile(),
            gameManager.getGame().getTablePile(), List.of(judge)));
        //TODO:think if sleep for judge is important so that player can see the card well before it get into discard pile
        try {
            sleep(JUDGE_PAUSE_ON_TABLE_PILE);
        } catch (InterruptedException e) {
            //ignore
        }

        moveCard(new CardMoveHandle(gameManager, "judge", null, null, gameManager.getGame().getTablePile(),
            gameManager.getGame().getDiscardPile(), List.of(judge)));

        return judge;
    }

    public void drawCard(CardDrawHandle cardDrawHandle) {
        gameManager.getEventManager().triggerEvent(EventTypeCardDraw.DRAW_CARD, cardDrawHandle);
        Player player = cardDrawHandle.getPlayer();
        int count = cardDrawHandle.getCount();
        CardPile drawDeck = cardDrawHandle.getDrawPile();



        List<Card> drawedCards = peekTopDrawPile(count);

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

        if (cardMoveHandle.getPlayer() == null) {
            gameManager.output(String.format("%d card(s) move from %s to %s",
                cardMoveMessage.cardIds().size(), from.getName(), to.getName()));
        } else {
            gameManager.output(String.format("Player %s moves %d card(s) from %s to %s",
                cardMoveHandle.getPlayer().getUsername(), cardMoveMessage.cardIds().size(),
                from.getName(), to.getName()));
        }


        List<String> affectedPlayers = new ArrayList<>();
        if (from.getType().isConcealed() && to.getType().isConcealed()) {
            //send message with infos to the affected player
            if (cardMoveHandle.getPlayer() != null) {
                affectedPlayers.add(cardMoveHandle.getPlayer().getUsername());
            }
            if (cardMoveHandle.getTo() != null) {
                affectedPlayers.add(cardMoveHandle.getTo().getUsername());
            }
            gameManager.getGameController().cardMove(gameManager.getLobbyId(), affectedPlayers, cardMoveMessage);

            //clear the card ids for other players if they were private
            cardMoveMessage.cardIds().clear();
        } else {
            gameManager.output(String.format("They are: %s",
                cardMoveHandle.getCardsToMove().stream().map(Card::toString).collect(Collectors.joining(",")))
            );
        }


        //send message to all / not affected players, card ids are empty if they were concealed
        List<String> notAffectedPlayers = gameManager.getGame().getAllPlayers().stream()
            .map(Player::getUsername).filter(username -> !affectedPlayers.contains(username)).toList();
        gameManager.getGameController().cardMove(gameManager.getLobbyId(), notAffectedPlayers, cardMoveMessage);
        try {
            sleep(CARD_MOVE_PAUSE_BETWEEN_MOVEMENT);
        } catch (InterruptedException e) {
            //ignore
        }
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
