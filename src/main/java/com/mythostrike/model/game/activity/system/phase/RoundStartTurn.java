package com.mythostrike.model.game.activity.system.phase;

import com.mythostrike.model.game.activity.Activity;
import com.mythostrike.model.game.management.GameManager;
import com.mythostrike.model.game.player.Player;

public class RoundStartTurn extends Activity {
    public static final String NAME = RoundStartTurn.class.getSimpleName();
    public static final String DESCRIPTION = "start of round";

    private final GameManager gameManager;

    public RoundStartTurn(GameManager gameManager) {
        super(NAME, DESCRIPTION);
        this.gameManager = gameManager;
    }

    @Override
    public void use() {
        Player player = gameManager.getGame().getCurrentPlayer();
        player.resetImmunity();
        player.resetRestrict();
    }
}
