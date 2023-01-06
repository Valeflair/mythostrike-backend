package core.skill.events.handle;

import core.game.management.GameManager;
import core.game.Phase;
import core.game.Player;

public class PhaseHandle extends EventHandle{
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
