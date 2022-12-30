package Core;

import java.util.ArrayList;
import java.util.List;

public enum ChampionData {
    ACHILLES("Achilles",4,new ArrayList<Skill>(List.of(new Skill(TriggerSkillData.REVENGE))),"")
    ,KRATOS("Kratos",5,new ArrayList<Skill>(),"")
    ,TERPSICHORE("Terpsichore",3,new ArrayList<Skill>(),"")
    ,POSEIDON("Poseidon",3,new ArrayList<Skill>(),"")
    ,MARS("Mars",4,new ArrayList<Skill>(),"")
    ,HERACLES("Heracles",4,new ArrayList<Skill>(),"")


    ;


    private final String name;
    private final int maxHp;
    private final ArrayList<Skill> skills;
    private final String picture;

    ChampionData(String name, int maxHp, ArrayList<Skill> skills, String picture) {
        this.name = name;
        this.maxHp = maxHp;
        this.skills = skills;
        this.picture = picture;
    }

    public String getName() {
        return name;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public ArrayList<Skill> getSkills() {
        return skills;
    }

    public String getPicture() {
        return picture;
    }

    public void addSkill(Skill skill){
        skills.add(skill);
    }

    public void detachSkill(Skill skill){
        skills.remove(skill);
    }
}
