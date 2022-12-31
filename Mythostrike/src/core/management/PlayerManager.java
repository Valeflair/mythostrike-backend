package core.management;

import core.Player;
import events.handle.DamageHandle;
import events.handle.DamageType;

public class PlayerManager {

    private GameManager gameManager;

    public PlayerManager(GameManager gameManager){
        this.gameManager = gameManager;
    }
    public void applyDamage(DamageHandle damageHandle){
        //TODO : call event for damage, positive damage for damage event, negative damage for heal event
        Player from = damageHandle.getFrom();
        Player to = damageHandle.getTo();
        int damage = damageHandle.getDamage();
        DamageType type = damageHandle.getDamageType();

        if(!damageHandle.isPrevented()){

            //reduce hp
            to.setCurrentHp(to.getCurrentHp() - damage);

            //output message
            String hint = "Player " + from.getName();
            hint += " deals " + damage + " ";
            if(!type.equals(DamageType.NORMAL)){
                hint += type.toString();
            }
            hint += " damage to Player" + to.getName();
            hint += ", ouch! And he has now " + to.getCurrentHp() + " HP.";
            gameManager.getGame().output(hint);
        }

    }
}
