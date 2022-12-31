package events.handle;

import core.Card;
import core.management.GameManager;
import core.Player;

public class PlayerHandle extends EventHandle{

    public PlayerHandle(GameManager gameManager, Card card, String reason, Player from) {
        super(gameManager, card, reason, from);
    }
}
