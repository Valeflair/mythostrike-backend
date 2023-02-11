package com.mythostrike.model.game.player;

import com.mythostrike.model.game.activity.ActiveSkill;
import com.mythostrike.model.game.activity.PassiveSkill;
import lombok.Getter;

import java.util.List;

@Getter
public class Champion {

    private int id;
    private final String name;
    private final int maxHp;
    private final List<PassiveSkill> passiveSkills;
    private final List<ActiveSkill> activeSkills;

    public Champion(int id, String name, int maxHp, List<PassiveSkill> passiveSkills,
                    List<ActiveSkill> activeSkills) {
        this.name = name;
        this.maxHp = maxHp;
        this.passiveSkills = List.copyOf(passiveSkills);
        this.activeSkills = List.copyOf(activeSkills);
    }

    public Champion(int id, ChampionData data) {
        this.id = id;
        this.name = data.getName();
        this.maxHp = data.getMaxHp();
        this.passiveSkills = List.copyOf(data.getPassiveSkills());
        this.activeSkills = List.copyOf(data.getActiveSkills());
    }

}
