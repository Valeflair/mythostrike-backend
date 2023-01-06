package core.skill.events.handle;

import core.game.management.GameManager;
import core.game.Player;

public class PlayerHandle extends EventHandle{

    public PlayerHandle(GameManager gameManager, String reason, Player from) {
        super(gameManager, null, reason, from);
    }
}
