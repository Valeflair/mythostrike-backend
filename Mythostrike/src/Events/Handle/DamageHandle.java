package Events.Handle;

import Core.Card;
import Core.GameController;
import Core.Player;


public class DamageHandle extends EventHandle {
    private Player to;
    private int damage;
    private boolean isPrevented;
    private DamageType damageType;

    public DamageHandle(GameController gameController, Card card, String reason, Player from, Player to, int damage, DamageType damageType) {
        super(gameController, card, reason, from);
        this.to = to;
        this.damage = damage;
        this.isPrevented = false;
        this.damageType = damageType;
    }

    public DamageHandle(GameController gameController, String reason, Player from, Player to) {
        super(gameController, null, reason, from);
        this.to = to;
        damage = 1;
        isPrevented = false;
        damageType = DamageType.NORMAL;
    }


    public Player getTo() {
        return to;
    }

    public void setTo(Player to) {
        this.to = to;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
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
