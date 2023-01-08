package core.game;

import core.skill.Skill;
import core.skill.events.handle.CardAskHandle;
import core.skill.events.handle.DamageHandle;

import java.util.ArrayList;
import java.util.List;

public enum ChampionData {



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
        List<Effect<?>> effects = new ArrayList<>();
        effects.add(new Effect<DamageHandle>());
        effects.add(new Effect<CardAskHandle>());
        for (Effect<?> effect : effects) {

        }
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
