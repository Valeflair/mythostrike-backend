package com.mythostrike.model.game.activity;

public abstract class ActiveSkill extends Activity {


    protected ActiveSkill(String name, String description) {
        super(name, description);
    }

    public void initialize(int id) {
        this.id = id;
    }
}
