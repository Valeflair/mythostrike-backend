package com.mythostrike.model.game.skill.events.handle;

import com.mythostrike.model.game.core.Player;
import com.mythostrike.model.game.core.management.GameManager;

public class PlayerHandle extends EventHandle {

    public PlayerHandle(GameManager gameManager, String reason, Player from) {
        super(gameManager, null, reason, from);
    }
}
