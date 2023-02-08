package com.mythostrike.model.game.activity.system.phase;

import com.mythostrike.model.game.activity.Activity;
import com.mythostrike.model.game.activity.events.handle.CardDrawHandle;
import com.mythostrike.model.game.management.GameManager;
import com.mythostrike.model.game.player.Player;

public class DrawTurn extends Activity {
    public static final String NAME = "Draw";
    public static final String DESCRIPTION = "Drawing it's card";
    public static final int ID = -11;
    public static final int CARD_COUNT_TURN_START = 2;

    private GameManager gameManager;

    public DrawTurn(GameManager gameManager) {
        super(ID, NAME, DESCRIPTION);
    }

    @Override
    public void use() {
        Player player = gameManager.getGame().getCurrentPlayer();
        gameManager.getCardManager().drawCard(
            new CardDrawHandle(gameManager, DESCRIPTION, player,
                CARD_COUNT_TURN_START, gameManager.getGame().getDrawPile()));
    }
}
