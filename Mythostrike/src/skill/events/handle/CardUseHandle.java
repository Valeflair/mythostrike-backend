package skill.events.handle;

import core.Card;
import core.management.GameManager;
import core.Player;

import java.util.ArrayList;

public class CardUseHandle extends EventHandle{

    ArrayList<Player> to;
    boolean isHandCard;
    boolean cardUseConfirmed;

    public CardUseHandle(GameManager gameManager, Card card, String reason, Player from, ArrayList<Player> to, boolean isHandCard) {
        super(gameManager, card, reason, from);
        this.to = to;
        this.isHandCard = isHandCard;
        cardUseConfirmed = false;
    }

    public CardUseHandle(GameManager gameManager, Card card, String reason, Player from, Player to, boolean isHandCard) {
        super(gameManager, card, reason, from);
        this.to = new ArrayList<Player>();
        this.to.add(to);
        this.isHandCard = isHandCard;
        cardUseConfirmed = false;
    }

    public ArrayList<Player> getTo() {
        return to;
    }

    public void setTo(ArrayList<Player> to) {
        this.to = to;
    }

    public boolean isHandCard() {
        return isHandCard;
    }

    public void setHandCard(boolean handCard) {
        isHandCard = handCard;
    }

    public boolean isCardUseConfirmed() {
        return cardUseConfirmed;
    }

    public void setCardUseConfirmed(boolean cardUseConfirmed) {
        this.cardUseConfirmed = cardUseConfirmed;
    }
}
