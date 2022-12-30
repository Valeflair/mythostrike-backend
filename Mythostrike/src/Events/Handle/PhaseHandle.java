package Events.Handle;

import Core.Card;
import Core.GameController;
import Core.Phase;
import Core.Player;

public class PhaseHandle extends EventHandle{
    private Phase phase;

    public PhaseHandle(GameController gameController, Card card, String reason, Player from, Phase phase) {
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
