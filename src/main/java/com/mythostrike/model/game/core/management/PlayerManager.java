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

    private final GameManager gameManager;

    public PlayerManager(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    public void applyDamage(DamageHandle damageHandle) {
        while (true) {
            EventManager eventManager = gameManager.getEventManager();
            if (damageHandle.getDamage() > 0) {

                eventManager.triggerEvent(EventTypeDamage.CONFIRM_DAMAGE, damageHandle);
                eventManager.triggerEvent(EventTypeDamage.DAMAGE_FORESEEN, damageHandle);
                eventManager.triggerEvent(EventTypeDamage.DAMAGE_CAUSED, damageHandle);


                if (!damageHandle.isPrevented()) {
                    eventManager.triggerEvent(EventTypeDamage.DAMAGE_INFLICTED, damageHandle);
                    eventManager.triggerEvent(EventTypeDamage.BEFORE_DAMAGE_DONE, damageHandle);
                    eventManager.triggerEvent(EventTypeDamage.DAMAGE_DONE, damageHandle);
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
                    eventManager.triggerEvent(EventTypeDamage.DAMAGE, damageHandle);
                    eventManager.triggerEvent(EventTypeDamage.DAMAGED, damageHandle);
                    eventManager.triggerEvent(EventTypeDamage.DAMAGE_COMPLETE, damageHandle);
                }

            } else {
                eventManager.triggerEvent(EventTypeDamage.BEFORE_HP_RECOVER, damageHandle);
                //increase HP
                if (damageHandle.getDamage() > 0) {

                    continue;
                }
                if (damageHandle.getDamage() == 0) {
                    return;
                }
                damageHandle.getTo().increaseCurrentHp(Math.abs(damageHandle.getDamage()));
                eventManager.triggerEvent(EventTypeDamage.AFTER_HP_RECOVER, damageHandle);
            }
            eventManager.triggerEvent(EventTypeDamage.HP_CHANGED, damageHandle);

            return;
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

    public void killPlayer(Player player) {
        //TODO:update information to frontend

        //TODO:implement
    }

}
