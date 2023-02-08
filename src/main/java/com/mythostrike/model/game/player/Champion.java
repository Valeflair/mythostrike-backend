package com.mythostrike.model.game.player;

import com.mythostrike.model.game.activity.ActiveSkill;
import com.mythostrike.model.game.activity.PassiveSkill;
import lombok.Getter;

import java.util.List;

@Getter
public class Champion {

    private static List<Champion> championPatterns;
    private final String name;
    private final String picture;
    private final int maxHp;
    private final List<PassiveSkill> passiveSkills;
    private final List<ActiveSkill> activeSkills;

    public Champion(String name, String picture, int maxHp, List<PassiveSkill> passiveSkills,
                    List<ActiveSkill> activeSkills) {
        this.name = name;
        this.picture = picture;
        this.maxHp = maxHp;
        this.passiveSkills = passiveSkills;
        this.activeSkills = activeSkills;
    }


    public static List<Champion> getChampionPatterns() {
        return championPatterns;
    }

    public static void setChampionPatterns(List<Champion> championPatterns) {
        Champion.championPatterns = championPatterns;
    }

}
