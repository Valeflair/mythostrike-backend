package Core;

public class Damage {
    Player from;
    Player to;
    Card card;
    int damage;
    String reason;
    boolean isPrevented;
    DamageType damageType;

    public Damage() {
    }

    public Damage(Player from, Player to, Card card, int damage, DamageType damageType) {
        this.from = from;
        this.to = to;
        this.card = card;
        this.damage = damage;
        this.damageType = damageType;
        isPrevented = false;
    }

    public Damage(Player from, Player to, int damage, String reason, DamageType damageType) {
        this.from = from;
        this.to = to;
        this.damage = damage;
        this.reason = reason;
        this.damageType = damageType;
        isPrevented = false;
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
}
