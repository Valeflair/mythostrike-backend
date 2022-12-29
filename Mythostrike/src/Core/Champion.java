package Core;

import java.util.ArrayList;

public class Champion {

    private static ArrayList<Champion> allChampion = new ArrayList<Champion>();
    private String name;
    private String picture;
    private int maxHp;
    private ArrayList<Skill> skills;

    public Champion(ChampionData data){
        name = data.getName();
        picture = data.getPicture();
        maxHp = data.getMaxHp();
        skills = data.getSkills();
    }

    public static ArrayList<Champion> getAllChampion() {
        return allChampion;
    }

    public static void setAllChampion(ArrayList<Champion> allChampion) {
        Champion.allChampion = allChampion;
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
