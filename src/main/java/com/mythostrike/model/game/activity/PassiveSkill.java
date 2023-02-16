package com.mythostrike.model.game.activity;


import com.mythostrike.model.game.management.EventManager;
import com.mythostrike.model.game.player.Player;
import lombok.Getter;

@Getter
public abstract class PassiveSkill extends Activity {


    protected PassiveSkill(String name, String description) {
        super(name, description);
    }

    public abstract void register(EventManager eventManager, Player player);

}
