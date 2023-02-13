package com.mythostrike.model.game.player;

import com.mythostrike.model.game.activity.ActiveSkill;
import com.mythostrike.model.game.activity.PassiveSkill;
import com.mythostrike.model.game.activity.skill.active.Warrior;
import com.mythostrike.model.game.activity.skill.passive.Revenge;
import com.mythostrike.model.game.activity.skill.passive.Strength;
import lombok.Getter;

import java.util.List;

@Getter
public enum ChampionData {
    ARES("Ares", 4, List.of(new Warrior(1)), List.of(new Strength(2))),
    Achilles("Achilles", 4, List.of(), List.of(new Revenge(3)));

    private final String name;
    private final int maxHp;
    private final List<ActiveSkill> activeSkills;
    private final List<PassiveSkill> passiveSkills;

    ChampionData(String name, int maxHp, List<ActiveSkill> activeSkills, List<PassiveSkill> passiveSkills) {
        this.name = name;
        this.maxHp = maxHp;

        this.activeSkills = activeSkills;
        this.passiveSkills = passiveSkills;
    }


}
