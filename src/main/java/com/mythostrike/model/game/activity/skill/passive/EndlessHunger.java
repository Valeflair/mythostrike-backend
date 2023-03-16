package com.mythostrike.model.game.activity.skill.passive;

import com.mythostrike.model.game.activity.PassiveSkill;
import com.mythostrike.model.game.activity.cards.CardSymbol;
import com.mythostrike.model.game.activity.cards.cardtype.Defend;
import com.mythostrike.model.game.activity.cards.cardtype.Heal;
import com.mythostrike.model.game.activity.events.handle.CardFilterHandle;
import com.mythostrike.model.game.activity.events.type.EventTypeFilter;
import com.mythostrike.model.game.management.EventManager;
import com.mythostrike.model.game.player.Player;

public class EndlessHunger extends PassiveSkill {

    public static final String NAME = "Endless Hunger";
    public static final String DESCRIPTION = "You can't be healed. You can play your Heal cards as Defend cards.";

    private CardFilterHandle cardFilterHandle;

    public EndlessHunger() {
        super(NAME, DESCRIPTION);
    }


    @Override
    public void register(EventManager eventManager, Player player) {
        eventManager.registerEvent(EventTypeFilter.CARD_FILTER, this, player, true);
        eventManager.getGameManager().getPlayerManager().addSkillToPlayer(player, new Unhealable());
    }

    @Override
    public boolean checkCondition(CardFilterHandle cardFilterHandle) {
        if (cardFilterHandle.getPlayer().getPassiveSkills().stream()
            .anyMatch(passiveSkill -> passiveSkill.getName().equals(NAME))
            && cardFilterHandle.getCardFilter().match((new Defend(-1, CardSymbol.NO_SYMBOL, 0)))) {
            this.cardFilterHandle = cardFilterHandle;
            return true;
        }
        return false;
    }

    @Override
    public void activate() {
        cardFilterHandle.getCardFilter().addIncludeFilter(Heal.NAME);
    }

}
