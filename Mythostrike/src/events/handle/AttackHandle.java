package events.handle;

import core.Card;
import core.CardData;
import core.management.GameManager;
import core.Player;

public class AttackHandle extends EventHandle{

    Player to;
    Card defend;
    int extraDamage;
    boolean isPrevented;
    CardAskHandle defendAskHandle;
    int targetCount;
    DamageHandle damageHandle;

    public AttackHandle(GameManager gameManager, Card card, String reason, Player from, Player to, Card defend, int extraDamage) {
        super(gameManager, card, reason, from);
        this.to = to;
        this.defend = defend;
        this.extraDamage = extraDamage;
        isPrevented = false;
        defendAskHandle = new CardAskHandle(gameManager, card, "you get attacked, drop a defend or you get damage", to, CardData.DEFEND, 1, gameManager.getGame().getThrowDeck(), true);
        targetCount = 1;
    }

    public Player getTo() {
        return to;
    }

    public void setTo(Player to) {
        this.to = to;
    }

    public Card getDefend() {
        return defend;
    }

    public void setDefend(Card defend) {
        this.defend = defend;
    }

    public int getExtraDamage() {
        return extraDamage;
    }

    public void setExtraDamage(int extraDamage) {
        this.extraDamage = extraDamage;
    }

    public boolean isPrevented() {
        return isPrevented;
    }

    public void setPrevented(boolean prevented) {
        isPrevented = prevented;
    }

    public CardAskHandle getDefendAskHandle() {
        return defendAskHandle;
    }

    public void setDefendAskHandle(CardAskHandle defendAskHandle) {
        this.defendAskHandle = defendAskHandle;
    }

    public int getTargetCount() {
        return targetCount;
    }

    public void setTargetCount(int targetCount) {
        this.targetCount = targetCount;
    }

    public DamageHandle getDamageHandle() {
        return damageHandle;
    }

    public void setDamageHandle(DamageHandle damageHandle) {
        this.damageHandle = damageHandle;
    }
}
