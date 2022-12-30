package Events;

import Core.Card;
import Core.GameController;
import Core.Phase;
import Core.Player;
import Events.Handle.EventHandle;

public class PhaseChangeHandle extends EventHandle {
    Phase before;
    Phase after;

    public PhaseChangeHandle(GameController gameController, Card card, String reason, Player from, Phase before, Phase after) {
        super(gameController, card, reason, from);
        this.before = before;
        this.after = after;
    }
}
