package com.mythostrike.model.game.core.activity;


import com.mythostrike.model.game.core.Player;
import com.mythostrike.model.game.core.management.EventManager;

public abstract class PassiveSkill extends Activity {

    private Player player;

    public PassiveSkill(String name, String description, Player player) {
        super(name, description);
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public void register(EventManager eventManager) {
    }
}
