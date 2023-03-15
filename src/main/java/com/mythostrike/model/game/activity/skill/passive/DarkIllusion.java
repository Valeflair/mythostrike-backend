package com.mythostrike.model.game.activity.skill.passive;

import com.mythostrike.model.game.activity.PassiveSkill;
import com.mythostrike.model.game.activity.cards.CardSymbol;
import com.mythostrike.model.game.activity.cards.cardtype.Defend;
import com.mythostrike.model.game.activity.events.handle.CardFilterHandle;
import com.mythostrike.model.game.activity.events.type.EventTypeFilter;
import com.mythostrike.model.game.management.EventManager;
import com.mythostrike.model.game.player.Player;

public class DarkIllusion extends PassiveSkill {

    public static final String NAME = "Dark Illusion";
    public static final String DESCRIPTION = "You can play black cards as Defend cards.";
    CardFilterHandle cardFilterHandle;

    public DarkIllusion() {
        super(NAME, DESCRIPTION);
    }

    @Override
    public void register(EventManager eventManager, Player player) {
        eventManager.registerEvent(EventTypeFilter.CARD_FILTER, this, player, true);
    }

    @Override
    public boolean checkCondition(CardFilterHandle cardFilterHandle) {
        if (cardFilterHandle.getCardFilter().match(new Defend(-1, CardSymbol.NO_SYMBOL, -1))
            && cardFilterHandle.getPlayer().getPassiveSkills().stream()
            .anyMatch(passiveSkill -> passiveSkill.getName().equals(NAME))) {
            this.cardFilterHandle = cardFilterHandle;
            return true;
        }
        return false;
    }

    @Override
    public void activate() {
        cardFilterHandle.getCardFilter().addIncludeSymbol(CardSymbol.CLUB);
        cardFilterHandle.getCardFilter().addIncludeSymbol(CardSymbol.SPADE);
    }
}
