package com.mythostrike.model.game.activity.system.phase;

import com.mythostrike.model.game.activity.Activity;
import com.mythostrike.model.game.activity.events.handle.CardDrawHandle;
import com.mythostrike.model.game.management.GameManager;
import com.mythostrike.model.game.player.Player;

public class DrawTurn extends Activity {
    public static final String NAME = DrawTurn.class.getSimpleName();
    public static final String DESCRIPTION = "Turn to draw";
    public static final int ID = -21;
    public static final int CARD_COUNT_TURN_START = 2;

    private final GameManager gameManager;

    public DrawTurn(GameManager gameManager) {
        super(ID, NAME, DESCRIPTION);
        this.gameManager = gameManager;
    }

    @Override
    public void use() {
        Player player = gameManager.getGame().getCurrentPlayer();
        gameManager.getCardManager().drawCard(
            new CardDrawHandle(gameManager, DESCRIPTION, player,
                CARD_COUNT_TURN_START, gameManager.getGame().getDrawPile()));
    }
}
