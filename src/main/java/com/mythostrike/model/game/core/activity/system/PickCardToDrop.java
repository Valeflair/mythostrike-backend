package com.mythostrike.model.game.core.activity.system;

import com.mythostrike.model.game.core.HighlightMessage;
import com.mythostrike.model.game.core.activity.Activity;
import com.mythostrike.model.game.core.activity.Card;
import com.mythostrike.model.game.core.management.GameManager;
import com.mythostrike.model.game.core.player.Player;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class PickCardToDrop extends Activity {
    public static final String NAME = "Draw";
    public static final String DESCRIPTION = "Drawing it's card";
    public static final int ID = -11;
    public static final int CARD_COUNT_TURN_START = 2;

    private GameManager gameManager;
    @Setter
    private int amount;

    public PickCardToDrop(GameManager gameManager, int amount) {
        super(ID, NAME, DESCRIPTION);
        this.amount = amount;
    }

    @Override
    public void use() {

        Player player = gameManager.getGame().getCurrentPlayer();
        List<Card> cards = player.getHandCards().getCards();
        List<Integer> cardIds = new ArrayList<>();
        for (Card card : cards) {
            cardIds.add(card.getId());
        }
        HighlightMessage highlightMessage = new HighlightMessage(cardIds, null,
            null, amount, amount, 0, 0, "Pick Card to drop", false);
        PickRequest pickRequest = new PickRequest(player, gameManager, highlightMessage);
        DropCard dropCard = new DropCard(gameManager, pickRequest);
        gameManager.queueActivity(dropCard);
        gameManager.queueActivity(pickRequest);
        //TODO implement with highlightmessage
        //highlightmessage should contain a class PlayCard added into gamemanager and use setTarget or setCard before call back


    }

}
