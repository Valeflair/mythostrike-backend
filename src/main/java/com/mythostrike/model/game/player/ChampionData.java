package com.mythostrike.model.game.player;

import com.mythostrike.model.game.activity.ActiveSkill;
import com.mythostrike.model.game.activity.PassiveSkill;
import com.mythostrike.model.game.activity.skill.passive.Revenge;
import lombok.Getter;

import java.util.List;

@Getter
public enum ChampionData {
    Ares("Ares", 4, List.of(), List.of(new Revenge(1)), "");

    private final String name;
    private final int maxHp;
    private final List<ActiveSkill> activeSkills;
    private final List<PassiveSkill> passiveSkills;
    private final String picture;

    ChampionData(String name, int maxHp, List<ActiveSkill> activeSkills, List<PassiveSkill> passiveSkills,
                 String picture) {
        this.name = name;
        this.maxHp = maxHp;

        this.activeSkills = activeSkills;
        this.passiveSkills = passiveSkills;
        this.picture = picture;
    }


}
