package com.mythostrike.model.game.activity.system.phase;

import com.mythostrike.model.game.activity.Activity;
import com.mythostrike.model.game.activity.system.PickCardToDrop;
import com.mythostrike.model.game.management.GameManager;
import com.mythostrike.model.game.player.Player;

public class DropTurn extends Activity {
    public static final String NAME = Class.class.getSimpleName();
    public static final String DESCRIPTION = "Turn to drop";
    public static final int ID = -22;
    public static final int CARD_COUNT_TURN_START = 2;

    private final GameManager gameManager;

    public DropTurn(GameManager gameManager) {
        super(ID, NAME, DESCRIPTION);
        this.gameManager = gameManager;
    }

    @Override
    public void use() {
        Player player = gameManager.getGame().getCurrentPlayer();
        if (player.getHandCards().size() > player.getCurrentHp()) {
            gameManager.queueActivity(new PickCardToDrop(gameManager,
                player.getHandCards().size() - player.getCurrentHp()));
        }

    }


}
