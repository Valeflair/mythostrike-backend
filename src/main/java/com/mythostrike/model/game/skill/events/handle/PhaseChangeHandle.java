package com.mythostrike.model.game.skill.events.handle;

import com.mythostrike.model.game.core.Phase;
import com.mythostrike.model.game.core.Player;
import com.mythostrike.model.game.core.management.GameManager;

public class PhaseChangeHandle extends EventHandle {
    Phase before;
    Phase after;

    public PhaseChangeHandle(GameManager gameController, String reason, Player from, Phase before, Phase after) {
        super(gameController, null, reason, from);
        this.before = before;
        this.after = after;
    }
}
