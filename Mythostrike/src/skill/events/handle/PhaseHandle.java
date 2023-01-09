package skill.events.handle;

import core.management.GameManager;
import core.Phase;
import core.Player;

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
