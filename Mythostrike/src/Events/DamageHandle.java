package Events;

import Core.Card;
import Core.GameController;
import Core.Player;

public class DamageHandle extends EventHandle<DamageHandle> {
    Player from;
    Player to;
    Card card;
    int damage = 1;
    String reason = "None";
    boolean isPrevented = false;
    DamageType damageType = DamageType.NORMAL;

    public DamageHandle(Player from, Player to, Card card, int damage, String reason, DamageType damageType, GameController gameController) {
        this.from = from;
        this.to = to;
        this.card = card;
        this.damage = damage;
        this.reason = reason;
        super.setGameController(gameController);
    }

    public DamageHandle() {
    }

    public Player getFrom() {
        return from;
    }

    public void setFrom(Player from) {
        this.from = from;
    }

    public Player getTo() {
        return to;
    }

    public void setTo(Player to) {
        this.to = to;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public boolean isPrevented() {
        return isPrevented;
    }

    public void setPrevented(boolean prevented) {
        isPrevented = prevented;
    }

    public DamageType getDamageType() {
        return damageType;
    }

    public void setDamageType(DamageType damageType) {
        this.damageType = damageType;
    }
}
