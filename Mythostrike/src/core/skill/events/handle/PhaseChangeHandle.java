package core.skill.events.handle;

import core.game.management.GameManager;
import core.game.Phase;
import core.game.Player;

public class PhaseChangeHandle extends EventHandle {
    Phase before;
    Phase after;

    public PhaseChangeHandle(GameManager gameController, String reason, Player from, Phase before, Phase after) {
        super(gameController, null, reason, from);
        this.before = before;
        this.after = after;
    }
}
