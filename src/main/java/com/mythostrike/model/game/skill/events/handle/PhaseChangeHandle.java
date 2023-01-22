package skill.events.handle;

import core.management.GameManager;
import core.Phase;
import core.Player;

public class PhaseChangeHandle extends EventHandle {
    Phase before;
    Phase after;

    public PhaseChangeHandle(GameManager gameController, String reason, Player from, Phase before, Phase after) {
        super(gameController, null, reason, from);
        this.before = before;
        this.after = after;
    }
}
