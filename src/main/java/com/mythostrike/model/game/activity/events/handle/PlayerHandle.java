package com.mythostrike.model.game.activity.events.handle;

import com.mythostrike.model.game.management.GameManager;
import com.mythostrike.model.game.player.Player;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlayerHandle extends EventHandle {
    public PlayerHandle(GameManager gameManager, String reason, Player from) {
        super(gameManager, reason, from);
    }
}
