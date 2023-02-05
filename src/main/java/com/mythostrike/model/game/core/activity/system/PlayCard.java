package com.mythostrike.model.game.core.activity.system;

import com.mythostrike.model.game.core.activity.Activity;
import com.mythostrike.model.game.core.activity.Card;
import com.mythostrike.model.game.core.activity.events.handle.CardUseHandle;
import com.mythostrike.model.game.core.management.GameManager;
import lombok.Getter;

@Getter
public class PlayCard extends Activity {
    public static final String NAME = "Draw";
    public static final String DESCRIPTION = "Drawing it's card";
    public static final int ID = -11;
    public static final int CARD_COUNT_TURN_START = 2;

    private GameManager gameManager;
    private PickRequest pickRequest;

    public PlayCard(GameManager gameManager) {
        super(ID, NAME, DESCRIPTION);
    }

    @Override
    public void use() {
        if (pickRequest.getSelectedCards() == null || pickRequest.getSelectedCards().size() == 0) {
            return;
        } else {
            gameManager.getCurrentActivity().add(new PickCardToPLay(gameManager));
            for (Card card : pickRequest.getSelectedCards()) {
                card.activate();
            }

        }
    }
}
