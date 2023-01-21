package core.game.management;

import core.game.Player;
import core.skill.events.handle.DamageHandle;
import core.skill.events.handle.DamageType;
import core.skill.events.type.EventTypeDamage;

public class PlayerManager {

    private GameManager gameManager;

    public PlayerManager(GameManager gameManager){
        this.gameManager = gameManager;
    }
    public void applyDamage(DamageHandle damageHandle){
        if (damageHandle.getDamage() > 0) {
            gameManager.getEventManager().triggerEvent(EventTypeDamage.CONFIRM_DAMAGE, damageHandle);
            gameManager.getEventManager().triggerEvent(EventTypeDamage.DAMAGE_FORESEEN, damageHandle);
            gameManager.getEventManager().triggerEvent(EventTypeDamage.DAMAGE_CAUSED, damageHandle);
            if (!damageHandle.isPrevented()) {
                gameManager.getEventManager().triggerEvent(EventTypeDamage.DAMAGE_INFLICTED, damageHandle);
                gameManager.getEventManager().triggerEvent(EventTypeDamage.BEFORE_DAMAGE_DONE, damageHandle);
                gameManager.getEventManager().triggerEvent(EventTypeDamage.DAMAGE_DONE, damageHandle);
                //reduce hp
                damageHandle.getTo().setCurrentHp(damageHandle.getTo().getCurrentHp() - damageHandle.getDamage());

                //output message
                String hint = "Player " + damageHandle.getFrom().getName();
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
                gameManager.getEventManager().triggerEvent(EventTypeDamage.HP_CHANGED, damageHandle);
            }

        } else {
            gameManager.getEventManager().triggerEvent(EventTypeDamage.BEFORE_HP_RECOVER, damageHandle);
            damageHandle.getTo().setCurrentHp(damageHandle.getTo().getCurrentHp() - damageHandle.getDamage());
            String hint = "Player " + damageHandle.getTo().getName();
            hint += " heals " + damageHandle.getDamage() + " ";
            if (!damageHandle.getDamageType().equals(DamageType.NORMAL)) {
                hint += damageHandle.getDamageType().toString();
            }
            hint += " HP" + damageHandle.getTo().getName();
            hint += ", And he has now " + damageHandle.getTo().getCurrentHp() + " HP.";
            gameManager.getEventManager().triggerEvent(EventTypeDamage.AFTER_HP_RECOVER, damageHandle);
            gameManager.getEventManager().triggerEvent(EventTypeDamage.HP_CHANGED, damageHandle);
        }
    }
    private boolean playerDying(Player player) {
        if (player.getCurrentHp() )

        return false;
    }
}
