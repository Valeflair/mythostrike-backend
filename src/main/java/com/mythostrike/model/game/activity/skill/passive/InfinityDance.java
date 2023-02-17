package com.mythostrike.model.game.activity.skill.passive;

import com.mythostrike.model.game.activity.PassiveSkill;
import com.mythostrike.model.game.activity.cards.CardType;
import com.mythostrike.model.game.activity.events.handle.CardDrawHandle;
import com.mythostrike.model.game.activity.events.handle.CardUseHandle;
import com.mythostrike.model.game.activity.events.type.EventTypeCardUse;
import com.mythostrike.model.game.management.EventManager;
import com.mythostrike.model.game.player.Player;

public class InfinityDance extends PassiveSkill {

    public static final String NAME = InfinityDance.class.getSimpleName();
    public static final String DESCRIPTION = "you draw 1 card whenever you plays a skill card";

    private CardUseHandle cardUseHandle;

    public InfinityDance() {
        super(NAME, DESCRIPTION);
    }

    @Override
    public void register(EventManager eventManager, Player player) {
        eventManager.registerEvent(EventTypeCardUse.CARD_USED, this, player, true);
    }

    @Override
    public boolean checkCondition(CardUseHandle cardUseHandle) {
        if (cardUseHandle.getPlayer().getPassiveSkills().stream()
                .anyMatch(passiveSkill -> passiveSkill.getName().equals(NAME))
        && cardUseHandle.getCard().getType().equals(CardType.SKILL_CARD)) {
            this.cardUseHandle = cardUseHandle;
            return true;
        }

        return super.checkCondition(cardUseHandle);
    }

    @Override
    public void activate() {
        cardUseHandle.getGameManager().getCardManager().drawCard(new CardDrawHandle(
                cardUseHandle.getGameManager(),
                "activate InfinityDance",
                cardUseHandle.getPlayer(),
                1,
                cardUseHandle.getGameManager().getGame().getDrawPile()
        ));
    }
}
