package events.handle;

import core.Card;
import core.management.GameManager;
import core.Phase;
import core.Player;

public class PhaseHandle extends EventHandle{
    private Phase phase;

    public PhaseHandle(GameManager gameController, Card card, String reason, Player from, Phase phase) {
        super(gameController, card, reason, from);
        this.phase = phase;
    }

    public Phase getPhase() {
        return phase;
    }

    public void setPhase(Phase phase) {
        this.phase = phase;
    }
}
