package com.mythostrike.model.game.skill.events.handle;


import com.mythostrike.model.game.core.Phase;
import com.mythostrike.model.game.core.Player;
import com.mythostrike.model.game.core.management.GameManager;

public class PhaseHandle extends EventHandle {
    private Phase phase;

    public PhaseHandle(GameManager gameManager, String reason, Player from, Phase phase) {
        super(gameManager, null, reason, from);
        this.phase = phase;
    }

    public Phase getPhase() {
        return phase;
    }

    public void setPhase(Phase phase) {
        this.phase = phase;
    }
}
