package com.mythostrike.model.game.activity.system;

import com.mythostrike.model.game.activity.Activity;
import com.mythostrike.model.game.activity.Card;
import com.mythostrike.model.game.management.GameManager;
import lombok.Getter;

import java.util.HashMap;

@Getter
public class PlayCard extends Activity {
    public static final String NAME = PlayCard.class.getSimpleName();
    public static final String DESCRIPTION = "Play a card";
    public static final int ID = -17;
    public static final int CARD_COUNT_TURN_START = 2;

    private final GameManager gameManager;
    private final PickRequest pickRequest;

    public PlayCard(GameManager gameManager, PickRequest pickRequest) {
        super(ID, NAME, DESCRIPTION);
        this.gameManager = gameManager;
        this.pickRequest = pickRequest;
    }

    @Override
    public void use() {
        if (pickRequest.getSelectedCards() == null || pickRequest.getSelectedCards().size() == 0) {

        } else {
            gameManager.getCurrentActivity().addFirst(new PickCardToPLay(gameManager));
            for (Card card : pickRequest.getSelectedCards()) {
                HashMap<String, Integer> restrict = pickRequest.getPlayer().getRestrict();
                restrict.put(card.getName(), restrict.get(card.getName()) - 1);
                card.activate();
            }
        }
    }
}
