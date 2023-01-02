package core;

import skill.Skill;

import java.util.ArrayList;

public class Champion {

    private static ArrayList<Champion> championPatterns = new ArrayList<Champion>();
    private final String name;
    private final String picture;
    private final int maxHp;
    private final ArrayList<Skill> skills;

    public Champion(ChampionData data){
        name = data.getName();
        picture = data.getPicture();
        maxHp = data.getMaxHp();
        skills = data.getSkills();
    }

    public static ArrayList<Champion> getChampionPatterns() {
        return championPatterns;
    }

    public static void setChampionPatterns(ArrayList<Champion> championPatterns) {
        Champion.championPatterns = championPatterns;
    }

    public String getName() {
        return name;
    }

    public String getPicture() {
        return picture;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public ArrayList<Skill> getSkills() {
        return skills;
    }
}
