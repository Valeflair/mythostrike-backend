package core.management;

import skill.events.handle.DamageHandle;
import skill.events.handle.DamageType;

public class PlayerManager {

    private GameManager gameManager;

    public PlayerManager(GameManager gameManager){
        this.gameManager = gameManager;
    }
    public void applyDamage(DamageHandle damageHandle){
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
            gameManager.getEventManager().getConfirmDamage().onEvent(damageHandle);
            gameManager.getEventManager().getDamageForeseen().onEvent(damageHandle);
            gameManager.getEventManager().getDamageCaused().onEvent(damageHandle);



            if (!damageHandle.isPrevented()) {
                gameManager.getEventManager().getDamageInflicted().onEvent(damageHandle);
                gameManager.getEventManager().getBeforeDamageDone().onEvent(damageHandle);
                gameManager.getEventManager().getDamageDone().onEvent(damageHandle);
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
                gameManager.getEventManager().getDamage().onEvent(damageHandle);
                gameManager.getEventManager().getDamaged().onEvent(damageHandle);
                gameManager.getEventManager().getDamageComplete().onEvent(damageHandle);
            }

        } else {
            //TODO heal
        }

    }
}
