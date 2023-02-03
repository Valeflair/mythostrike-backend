package com.mythostrike.model.game.core.management;


import com.mythostrike.model.game.core.activity.ActiveSkill;
import com.mythostrike.model.game.core.activity.PassiveSkill;
import com.mythostrike.model.game.core.activity.events.handle.DamageHandle;
import com.mythostrike.model.game.core.activity.events.handle.DamageType;
import com.mythostrike.model.game.core.activity.events.type.EventTypeDamage;
import com.mythostrike.model.game.core.player.Champion;
import com.mythostrike.model.game.core.player.Player;

import java.util.List;

public class PlayerManager {

    private GameManager gameManager;

    public PlayerManager(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    public void applyDamage(DamageHandle damageHandle) {
        if (damageHandle.getDamage() > 0) {
                    /*
    CONFIRM_DAMAGE,
    DAMAGE_FORESEEN,
    DAMAGE_CAUSED,
    DAMAGE_INFLICTED,
    BEFORE_DAMAGE_DONE,
    DAMAGE_DONE,
    DAMAGE,
    DAMAGED,
    DAMAGE_COMPLETE,
         */
            gameManager.getEventManager().triggerEvent(EventTypeDamage.CONFIRM_DAMAGE, damageHandle);
            gameManager.getEventManager().triggerEvent(EventTypeDamage.DAMAGE_FORESEEN, damageHandle);
            gameManager.getEventManager().triggerEvent(EventTypeDamage.DAMAGE_CAUSED, damageHandle);


            if (!damageHandle.isPrevented()) {
                gameManager.getEventManager().triggerEvent(EventTypeDamage.DAMAGE_INFLICTED, damageHandle);
                gameManager.getEventManager().triggerEvent(EventTypeDamage.BEFORE_DAMAGE_DONE, damageHandle);
                gameManager.getEventManager().triggerEvent(EventTypeDamage.DAMAGE_DONE, damageHandle);
                //reduce hp
                damageHandle.getTo().decreaseCurrentHp(damageHandle.getDamage());

                //output message
                String hint = "Player " + damageHandle.getPlayer().getName();
                hint += " deals " + damageHandle.getDamage() + " ";
                if (!damageHandle.getDamageType().equals(DamageType.NORMAL)) {
                    hint += damageHandle.getDamageType().toString();
                }
                hint += " damage to Player " + damageHandle.getTo().getName();
                hint += ", ouch! And he has now " + damageHandle.getTo().getCurrentHp() + " HP.";
                gameManager.getGame().output(hint);
                gameManager.getEventManager().triggerEvent(EventTypeDamage.DAMAGE, damageHandle);
                gameManager.getEventManager().triggerEvent(EventTypeDamage.DAMAGED, damageHandle);
                gameManager.getEventManager().triggerEvent(EventTypeDamage.DAMAGE_COMPLETE, damageHandle);
            }

        } else {
            //TODO heal
        }

    }

    public void initialChampions(Champion champion, Player player) {
        player.setChampion(champion);
        player.setMaxHp(champion.getMaxHp());
        for (PassiveSkill passiveSkill : champion.getPassiveSkills()) {
            addSkillToPlayer(player, passiveSkill);
        }
        for (ActiveSkill activeSkill : champion.getActiveSkills()) {
            addSkillToPlayer(player, activeSkill);
        }
    }

    public void addSkillToPlayer(Player player, PassiveSkill skill) {
        List<PassiveSkill> skills = player.getPassiveSkills();
        if (!skills.contains(skill)) {
            skills.add(skill);
            skill.register(gameManager.getEventManager(), player);
            //TODO:add output message
        }
    }

    public void addSkillToPlayer(Player player, ActiveSkill skill) {
        List<ActiveSkill> skills = player.getActiveSkills();
        if (!skills.contains(skill)) {
            skills.add(skill);
            //TODO:add output message
        }
    }
}
