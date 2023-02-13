package com.mythostrike.model.game.activity.skill.active;


import com.mythostrike.model.game.activity.ActiveSkill;
import com.mythostrike.model.game.activity.events.handle.DamageHandle;
import com.mythostrike.model.game.activity.skill.passive.Strength;

public class Warrior extends ActiveSkill {

    public static final String NAME = Warrior.class.getSimpleName();
    public static final String DESCRIPTION = "you can play defense as attack if you want";


    public Warrior(int id) {
        super(id, NAME, DESCRIPTION);
    }

    //TODO:implement
}
