package com.mythostrike.model.game.activity.events.handle;


import com.mythostrike.model.game.Phase;
import com.mythostrike.model.game.management.GameManager;
import com.mythostrike.model.game.player.Player;
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
