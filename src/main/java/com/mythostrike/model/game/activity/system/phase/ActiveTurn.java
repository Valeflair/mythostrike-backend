package com.mythostrike.model.game.activity.system.phase;

import com.mythostrike.model.game.activity.Activity;
import com.mythostrike.model.game.activity.system.PickCardToPLay;
import com.mythostrike.model.game.management.GameManager;
import com.mythostrike.model.game.player.Player;

public class ActiveTurn extends Activity {
    public static final String NAME = ActiveTurn.class.getSimpleName();
    public static final String DESCRIPTION = "active turn";
    public static final int ID = -19;
    public static final int CARD_COUNT_TURN_START = 2;

    private final GameManager gameManager;

    public ActiveTurn(GameManager gameManager) {
        super(ID, NAME, DESCRIPTION);
        this.gameManager = gameManager;
    }

    @Override
    public void use() {
        gameManager.cleanTable();
        Player player = gameManager.getGame().getCurrentPlayer();
        gameManager.queueActivity(new PickCardToPLay(gameManager));
    }
}
