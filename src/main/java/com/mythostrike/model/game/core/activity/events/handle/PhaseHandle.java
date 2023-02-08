package com.mythostrike.model.game.core.activity.events.handle;


import com.mythostrike.model.game.core.Phase;
import com.mythostrike.model.game.core.management.GameManager;
import com.mythostrike.model.game.core.player.Player;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PhaseHandle extends EventHandle {
    private Phase phase;

    public PhaseHandle(GameManager gameManager, String reason, Player from, Phase phase) {
        super(gameManager, reason, from);
        this.phase = phase;
    }

}
