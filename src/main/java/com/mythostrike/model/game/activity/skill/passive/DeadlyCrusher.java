package com.mythostrike.model.game.activity.skill.passive;

import com.mythostrike.model.game.Phase;
import com.mythostrike.model.game.activity.PassiveSkill;
import com.mythostrike.model.game.activity.cards.Card;
import com.mythostrike.model.game.activity.cards.cardtype.Attack;
import com.mythostrike.model.game.activity.events.handle.AttackHandle;
import com.mythostrike.model.game.activity.events.handle.CardAskHandle;
import com.mythostrike.model.game.activity.events.handle.PhaseHandle;
import com.mythostrike.model.game.activity.events.type.EventTypeAttack;
import com.mythostrike.model.game.activity.events.type.EventTypeCardAsk;
import com.mythostrike.model.game.activity.events.type.EventTypePhase;
import com.mythostrike.model.game.activity.system.PickRequest;
import com.mythostrike.model.game.management.EventManager;
import com.mythostrike.model.game.management.GameManager;
import com.mythostrike.model.game.player.Player;

public class DeadlyCrusher extends PassiveSkill {

    public static final String NAME = "Deadly Crusher";
    public static final String DESCRIPTION = "If you don’t have any weapons and armory, you can use “Attack” without limit";

    private CardAskHandle cardAskHandle;

    public DeadlyCrusher() {
        super(NAME, DESCRIPTION);
    }


    @Override
    public void register(EventManager eventManager, Player player) {
        eventManager.registerEvent(EventTypeCardAsk.CARD_ASKED, this, player, true);
    }

    @Override
    public boolean checkCondition(CardAskHandle cardAskHandle) {
        if (cardAskHandle.getPlayer().getPassiveSkills().stream().anyMatch(passiveSkill -> passiveSkill.getName().equals(NAME))
                && cardAskHandle.getPlayer().getEquipment().getCards().isEmpty()) {
            this.cardAskHandle = cardAskHandle;
            return true;
        }
        return false;
    }

    @Override
    public void activate() {
        cardAskHandle.getPlayer().setUseTime(Attack.NAME, 1);
    }
}
