package skill.events.handle;

import core.Card;
import core.management.GameManager;
import core.Player;

public class PlayerAskHandle extends EventHandle{
    public PlayerAskHandle(GameManager gameManager, Card card, String reason, Player from) {
        super(gameManager, card, reason, from);
    }
}
