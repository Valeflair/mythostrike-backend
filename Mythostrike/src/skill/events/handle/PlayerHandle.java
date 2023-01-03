package skill.events.handle;

import core.Card;
import core.management.GameManager;
import core.Player;

public class PlayerHandle extends EventHandle{

    public PlayerHandle(GameManager gameManager, String reason, Player from) {
        super(gameManager, null, reason, from);
    }
}
