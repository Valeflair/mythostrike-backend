package com.mythostrike.model.game.management;


import com.mythostrike.model.game.Phase;
import com.mythostrike.model.game.activity.ActiveSkill;
import com.mythostrike.model.game.activity.PassiveSkill;
import com.mythostrike.model.game.activity.events.handle.CardMoveHandle;
import com.mythostrike.model.game.activity.events.handle.DamageHandle;
import com.mythostrike.model.game.activity.events.handle.DamageType;
import com.mythostrike.model.game.activity.events.type.EventTypeDamage;
import com.mythostrike.model.game.player.Champion;
import com.mythostrike.model.game.player.Player;

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
                    String hint = "Player " + damageHandle.getPlayer().getUsername();
                    hint += " deals " + damageHandle.getDamage() + " ";
                    if (!damageHandle.getDamageType().equals(DamageType.NORMAL)) {
                        hint += damageHandle.getDamageType().toString();
                    }
                    hint += " damage to Player " + damageHandle.getTo().getUsername();
                    hint += ", ouch! And he has now " + damageHandle.getTo().getCurrentHp() + " HP.";
                    gameManager.getGame().gameManager.output(hint);
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

    public void initializeChampionForPlayer(Champion champion, Player player) {
        player.setChampion(champion);
        player.setMaxHp(champion.getMaxHp());
        player.increaseCurrentHp(champion.getMaxHp());
        for (PassiveSkill passiveSkill : champion.getPassiveSkills()) {
            addSkillToPlayer(player, passiveSkill);
        }
        for (ActiveSkill activeSkill : champion.getActiveSkills()) {
            addSkillToPlayer(player, activeSkill);
        }
    }

    public void addSkillToPlayer(Player player, PassiveSkill skill) {
        if (skill == null) {
            return;
        }

        List<PassiveSkill> skills = player.getPassiveSkills();
        if (!skills.contains(skill)) {
            skills.add(skill);
            skill.register(gameManager.getEventManager(), player);
            //TODO:add output message
        }
    }

    public void addSkillToPlayer(Player player, ActiveSkill skill) {
        if (skill == null) {
            return;
        }

        List<ActiveSkill> skills = player.getActiveSkills();
        if (!skills.contains(skill)) {
            skills.add(skill);
            //TODO:add output message
        }
    }

    public void removeSkillFromPlayer(Player player, ActiveSkill skill) {
        if (skill == null) {
            return;
        }

        List<ActiveSkill> skills = player.getActiveSkills();
        skills.remove(skill);
    }

    public void removeSkillFromPlayer(Player player, PassiveSkill skill) {
        if (skill == null) {
            return;
        }

        List<PassiveSkill> skills = player.getPassiveSkills();
        skills.remove(skill);
    }




    public void killPlayer(Player player) {

        gameManager.getCardManager().moveCard(
            new CardMoveHandle(gameManager, "die", player, null, player.getHandCards(),
                gameManager.getGame().getTablePile(), player.getHandCards().getCards())
        );

        gameManager.getCardManager().moveCard(
            new CardMoveHandle(gameManager, "die", player, null, player.getEquipment(),
                gameManager.getGame().getTablePile(), player.getEquipment().getCards())
        );

        gameManager.getCardManager().moveCard(
            new CardMoveHandle(gameManager, "die", player, null, player.getDelayedEffect(),
                gameManager.getGame().getTablePile(), player.getDelayedEffect().getCards())
        );

        if (gameManager.getGame().getCurrentPlayer().equals(player)) {
            gameManager.setPhase(Phase.ROUND_START);
        }
        gameManager.getGame().getAlivePlayers().remove(player);


        gameManager.checkGameOver();
    }

}
