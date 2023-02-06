package com.mythostrike.model.game.core.activity.system.phase;

import com.mythostrike.model.game.core.activity.Activity;
import com.mythostrike.model.game.core.activity.events.handle.PhaseHandle;
import com.mythostrike.model.game.core.activity.events.type.EventTypePhase;

import com.mythostrike.model.game.core.management.GameManager;
import com.mythostrike.model.game.core.player.Player;

public class RoundStartTurn extends Activity {
    public static final String NAME = "RoundStart";
    public static final String DESCRIPTION = "starts its round";
    public static final int ID = -11;

    private GameManager gameManager;

    public RoundStartTurn(GameManager gameManager) {
        super(ID, NAME, DESCRIPTION);
    }

    @Override
    public void use() {
        Player player = gameManager.getGame().getCurrentPlayer();
        player.resetImmunity();
        player.resetRestrict();
    }
}
