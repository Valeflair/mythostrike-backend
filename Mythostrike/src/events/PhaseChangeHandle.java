package events;

import core.Card;
import core.management.GameManager;
import core.Phase;
import core.Player;
import events.handle.EventHandle;

public class PhaseChangeHandle extends EventHandle {
    Phase before;
    Phase after;

    public PhaseChangeHandle(GameManager gameController, Card card, String reason, Player from, Phase before, Phase after) {
        super(gameController, card, reason, from);
        this.before = before;
        this.after = after;
    }
}
