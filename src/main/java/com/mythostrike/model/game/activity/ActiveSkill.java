package com.mythostrike.model.game.activity;

import com.mythostrike.controller.message.game.PlayerCondition;
import com.mythostrike.model.game.activity.events.handle.PlayerHandle;
import lombok.Getter;

public abstract class ActiveSkill extends Activity {
    @Getter
    protected PlayerCondition playerCondition;
    @Getter
    protected PlayerHandle playerHandle;
    protected ActiveSkill(String name, String description) {
        super(name, description);
    }

    public void initialize(int id) {
        this.id = id;
    }
}
