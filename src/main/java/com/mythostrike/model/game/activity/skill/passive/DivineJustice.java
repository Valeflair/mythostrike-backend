package com.mythostrike.model.game.activity.skill.passive;

import com.mythostrike.model.game.activity.PassiveSkill;
import com.mythostrike.model.game.activity.cards.Card;
import com.mythostrike.model.game.activity.events.handle.AttackHandle;
import com.mythostrike.model.game.activity.events.type.EventTypeAttack;
import com.mythostrike.model.game.activity.system.PickRequest;
import com.mythostrike.model.game.management.EventManager;
import com.mythostrike.model.game.management.GameManager;
import com.mythostrike.model.game.player.Player;

public class DivineJustice extends PassiveSkill {

    public static final String NAME = DivineJustice.class.getSimpleName();
    public static final String DESCRIPTION = "when you attack a player, you can judge, if it's red, your attack " +
        "can't be avoided by defend ";
    private AttackHandle attackHandle;

    private PickRequest pickRequest;


    public DivineJustice() {
        super(NAME, DESCRIPTION);
    }


    @Override
    public void register(EventManager eventManager, Player player) {
        eventManager.registerEvent(EventTypeAttack.ATTACK_EFFECTED, this, player, true);
    }

    @Override
    public boolean checkCondition(AttackHandle attackHandle) {
        if (attackHandle.getPlayer().getPassiveSkills().stream()
            .anyMatch(passiveSkill -> passiveSkill.getName().equals(NAME))) {
            this.attackHandle = attackHandle;
            return true;
        }
        return false;
    }

    @Override
    public void activate() {
        GameManager gameManager = attackHandle.getGameManager();
        Card card = gameManager.getCardManager().judge();
        gameManager.output("%s activated! judge result: ".formatted(NAME) + card.getSymbol());
        if (card.isRed()) {
            attackHandle.getDefendAskHandle().setAmount(999);
            attackHandle.getDefendAskHandle().setReason("you get attacked, cause he activated divine justice" +
                " and the judge result is red, so you can't avoid the attack");
        }

    }
}
