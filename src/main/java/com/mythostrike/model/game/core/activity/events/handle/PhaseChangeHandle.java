package com.mythostrike.model.game.core.activity.events.handle;

import com.mythostrike.model.game.core.Phase;
import com.mythostrike.model.game.core.management.GameManager;
import com.mythostrike.model.game.core.player.Player;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PhaseChangeHandle extends EventHandle {
    private Phase before;
    private Phase after;

    public PhaseChangeHandle(GameManager gameManager, String reason, Player from, Phase before, Phase after) {
        super(gameManager, reason, from);
        this.before = before;
        this.after = after;
    }
}
