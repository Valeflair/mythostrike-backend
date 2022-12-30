package Events.Handle;

import Core.Card;
import Core.GameController;
import Core.Player;

import java.util.ArrayList;

public class CardUseHandle extends EventHandle{

    ArrayList<Player> to;
    boolean isHandCard;

    public CardUseHandle(GameController gameController, Card card, String reason, Player from, ArrayList<Player> to, boolean isHandCard) {
        super(gameController, card, reason, from);
        this.to = to;
        this.isHandCard = isHandCard;
    }

    public CardUseHandle(GameController gameController, Card card, String reason, Player from, Player to, boolean isHandCard) {
        super(gameController, card, reason, from);
        this.to = new ArrayList<Player>();
        this.to.add(to);
        this.isHandCard = isHandCard;
    }
}
