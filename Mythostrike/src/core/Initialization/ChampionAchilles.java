package core.Initialization;

import core.game.Champion;
import core.game.Effect;
import core.skill.Skill;
import core.skill.events.handle.DamageHandle;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class ChampionAchilles extends Champion {
    private static final String name = "Achilles";
    private static final int maxHp = 4;
    private static final List<Skill> skills = new ArrayList<>();


    public ChampionAchilles() {
        super(name, maxHp, skills);
        Skill skill = new Skill("Revenge", "when you get a damage, you can judge a card, if it's not heart, the damage dealer has to drop 2 cards or get 1 damage dealed by you", false,
                new Effect<DamageHandle>("Revenge", new Function<DamageHandle, Boolean>() {
                    @Override
                    public Boolean apply(DamageHandle damageHandle) {
                        return null;
                    }
                }, new Function<DamageHandle, Boolean>() {
                    @Override
                    public Boolean apply(DamageHandle damageHandle) {
                        return null;
                    }
                }, new Function<DamageHandle, Boolean>() {
                    @Override
                    public Boolean apply(DamageHandle damageHandle) {
                        return null;
                    }
                }));
        skills.add(skill);
    }
}
