package com.mythostrike.model.game.activity.skill.equipment;

import com.mythostrike.model.game.activity.PassiveSkill;
import com.mythostrike.model.game.activity.cards.Card;
import com.mythostrike.model.game.activity.cards.cardtype.HeartOfTerra;
import com.mythostrike.model.game.activity.events.handle.CardMoveHandle;
import com.mythostrike.model.game.activity.events.handle.DamageHandle;
import com.mythostrike.model.game.activity.events.type.EventTypeDamage;
import com.mythostrike.model.game.management.EventManager;
import com.mythostrike.model.game.management.GameManager;
import com.mythostrike.model.game.player.Player;

import java.util.List;

public class HeartOfTerraSkill extends PassiveSkill {

    public static final String NAME = "Heart Of Terra Skill";
    public static final String DESCRIPTION = "It can absorb the first damage you get. "
        + "After that, it will be thrown away.";
    private final Card armor;
    private DamageHandle damageHandle;


    public HeartOfTerraSkill(HeartOfTerra armor) {
        super(NAME, DESCRIPTION);
        this.armor = armor;
    }

    @Override
    public void register(EventManager eventManager, Player player) {
        eventManager.registerEvent(EventTypeDamage.DAMAGE_FORESEEN, this, player, false);
    }

    @Override
    public boolean checkCondition(DamageHandle damageHandle) {

        if (damageHandle.getTo().getPassiveSkills().contains(this)) {
            this.damageHandle = damageHandle;
            return true;
        }
        return false;

    }

    @Override
    public void activate() {

        damageHandle.setPrevented(true);
        GameManager gameManager = damageHandle.getGameManager();
        Player player = damageHandle.getTo();

        gameManager.output("Heart of Terra absorbed the damage for %s and will be thrown away."
            .formatted(player.getUsername()));


        CardMoveHandle throwArmor = new CardMoveHandle(gameManager, "throw armor"
            , player, null, player.getEquipment(),
            gameManager.getGame().getTablePile(), List.of(armor));
        gameManager.getCardManager().moveCard(throwArmor);
        gameManager.getPlayerManager().removeSkillFromPlayer(player, armor.getPassiveSkill());
        gameManager.getPlayerManager().removeSkillFromPlayer(player, armor.getActiveSkill());
    }

}
