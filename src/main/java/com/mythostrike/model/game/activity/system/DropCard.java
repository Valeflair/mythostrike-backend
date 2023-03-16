package com.mythostrike.model.game.activity.system;

import com.mythostrike.model.game.activity.Activity;
import com.mythostrike.model.game.activity.cards.Card;
import com.mythostrike.model.game.management.GameManager;
import com.mythostrike.model.game.player.Player;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
public class DropCard extends Activity {
    public static final String NAME = "Drop Card";
    public static final String DESCRIPTION = "Drop a card.";
    public static final int CARD_COUNT_TURN_START = 2;

    private final GameManager gameManager;
    @Setter
    private PickRequest pickRequest;


    public DropCard(GameManager gameManager, PickRequest pickRequest) {
        super(NAME, DESCRIPTION);
        this.gameManager = gameManager;
        this.pickRequest = pickRequest;
    }

    @Override
    public void use() {
        Player player = gameManager.getGame().getCurrentPlayer();
        List<Card> cards = pickRequest.getSelectedCards();
        if (cards == null || cards.isEmpty()) {
            return;
        }
        gameManager.getCardManager().throwCard(player, cards, player.getHandCards());
    }
}
