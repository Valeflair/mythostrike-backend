package events.handle;

import core.*;
import core.management.GameManager;

public class CardAskHandle extends EventHandle{

    CardData cardData;
    int amount;
    boolean responeded;
    boolean optional;
    CardList targetSpace;

    public CardAskHandle(GameManager gameManager, Card card, String reason, Player from, CardData cardData, int amount, CardList targetSpace, boolean optional) {
        super(gameManager, card, reason, from);
        this.cardData = cardData;
        this.amount = amount;
        this.targetSpace = targetSpace;
        this.optional = optional;
        responeded = false;
    }

    public boolean respondAble() {
        // TODO utilize it to an attribute describe the allowance of another cardSpace, probably CardSpaceType will make sense
        Player player = getFrom();
        int count = 0;
        CardSpace cardSpace = player.getHandCards();
        for (Card card : cardSpace.getCards()) {
            // TODO consider View_As_Skill (Black as defend)
            if (cardData == null || card.isSame(cardData)) {
                count++;
            }
        }
        return count >= amount;
    }

    public CardData getCardData() {
        return cardData;
    }

    public void setCardData(CardData cardData) {
        this.cardData = cardData;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public boolean isResponeded() {
        return responeded;
    }

    public void setResponeded(boolean responeded) {
        this.responeded = responeded;
    }

    public boolean isOptional() {
        return optional;
    }

    public void setOptional(boolean optional) {
        this.optional = optional;
    }

    public CardList getTargetSpace() {
        return targetSpace;
    }

    public void setTargetSpace(CardList targetSpace) {
        this.targetSpace = targetSpace;
    }
}
