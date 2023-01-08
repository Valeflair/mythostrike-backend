package core.game;

import core.skill.Skill;

import java.util.List;

public class Champion {

    private static List<Champion> championPatterns;
    private final String name;
    private final int maxHp;
    private final List<Skill> skills;

    public Champion(String name, int maxHp, List<Skill> skills) {
        this.name = name;
        this.maxHp = maxHp;
        this.skills = skills;
    }

    public static List<Champion> getChampionPatterns() {
        return championPatterns;
    }

    public static void setChampionPatterns(List<Champion> championPatterns) {
        Champion.championPatterns = championPatterns;
    }

    public String getName() {
        return name;
    }


    public int getMaxHp() {
        return maxHp;
    }

    public List<Skill> getSkills() {
        return skills;
    }
}
