package skill.events.handle;

import core.activity.Card;
import core.management.GameManager;
import core.Player;

import java.util.ArrayList;
import java.util.List;

public class CardUseHandle extends EventHandle{

    List<Player> to;
    boolean isHandCard;
    boolean cardUseConfirmed;

    public CardUseHandle(GameManager gameManager, Card card, String reason, Player from, List<Player> to, boolean isHandCard) {
        super(gameManager, card, reason, from);
        this.to = to;
        this.isHandCard = isHandCard;
        cardUseConfirmed = false;
    }

    public CardUseHandle(GameManager gameManager, Card card, String reason, Player from, Player to, boolean isHandCard) {
        super(gameManager, card, reason, from);
        this.to = new ArrayList<>();
        this.to.add(to);
        this.isHandCard = isHandCard;
        cardUseConfirmed = false;
    }

    public List<Player> getTo() {
        return to;
    }

    public void setTo(List<Player> to) {
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
