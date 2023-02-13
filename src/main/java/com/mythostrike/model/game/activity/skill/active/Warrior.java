package com.mythostrike.model.game.activity.skill.active;


import com.mythostrike.model.game.activity.ActiveSkill;
import com.mythostrike.model.game.activity.events.handle.DamageHandle;

public class Warrior extends ActiveSkill {

    public static final String NAME = "Revenge";
    public static final String DESCRIPTION = "when you get damage, judge, if it's red, the damage dealer drops 1 card,"
            + " if it's black, the damage dealer get 1  damage by you";


    public Warrior(int id) {
        super(id, NAME, DESCRIPTION);
    }

    //TODO:implement
}
