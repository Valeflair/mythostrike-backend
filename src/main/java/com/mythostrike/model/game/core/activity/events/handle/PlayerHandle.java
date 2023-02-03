package com.mythostrike.model.game.core.activity.events.handle;

import com.mythostrike.model.game.core.player.Player;
import com.mythostrike.model.game.core.management.GameManager;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlayerHandle extends EventHandle {
    public PlayerHandle(GameManager gameManager, String reason, Player from) {
        super(gameManager, reason, from);
    }
}
