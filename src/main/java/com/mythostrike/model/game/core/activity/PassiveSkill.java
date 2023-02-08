package com.mythostrike.model.game.core.activity;


import com.mythostrike.model.game.core.management.EventManager;
import com.mythostrike.model.game.core.player.Player;
import lombok.Getter;

@Getter
public abstract class PassiveSkill extends Activity {


    protected PassiveSkill(int id, String name, String description) {
        super(id, name, description);
    }


    public abstract void register(EventManager eventManager, Player player);

    @Override
    public void activate() {
    }

}
