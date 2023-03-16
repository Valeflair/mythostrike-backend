package com.mythostrike.model.game.activity.system;

import com.mythostrike.controller.message.game.HighlightMessage;
import com.mythostrike.model.game.activity.Activity;
import com.mythostrike.model.game.activity.cards.Card;
import com.mythostrike.model.game.management.GameManager;
import com.mythostrike.model.game.player.Player;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
public class PickCardToDrop extends Activity {
    public static final String NAME = "Pick Card To Drop";
    public static final String DESCRIPTION = "Pick a card to drop.";
    public static final int CARD_COUNT_TURN_START = 2;

    private final GameManager gameManager;
    @Setter
    private int amount;

    public PickCardToDrop(GameManager gameManager, int amount) {
        super(NAME, DESCRIPTION);
        this.gameManager = gameManager;
        this.amount = amount;
    }

    @Override
    public void use() {

        Player player = gameManager.getGame().getCurrentPlayer();
        List<Card> cards = player.getHandCards().getCards();
        List<Integer> cardIds = GameManager.convertCardsToInteger(cards);
        HighlightMessage highlightMessage = HighlightMessage.builder()
            .cardIds(cardIds)
            .cardCount(List.of(amount))
            .reason("Pick %d Cards to drop".formatted(amount))
            .build();
        PickRequest pickRequest = new PickRequest(player, gameManager, highlightMessage);
        Activity dropCard = new DropCard(gameManager, pickRequest);
        gameManager.queueActivity(dropCard);
        gameManager.queueActivity(pickRequest);

    }

}
