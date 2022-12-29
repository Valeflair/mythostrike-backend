package Events;

import Core.GameController;
import Core.Phase;
import Core.Player;

public class PhaseChangeHandle extends EventHandle<PhaseChangeHandle> {
    Player player;
    Phase before;
    Phase after;

    public PhaseChangeHandle(Player player, Phase before, Phase after, GameController gameController) {
        this.player = player;
        this.before = before;
        this.after = after;
        super.setGameController(gameController);
    }
}
