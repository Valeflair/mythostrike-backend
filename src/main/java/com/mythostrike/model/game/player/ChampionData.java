package com.mythostrike.model.game.player;

import com.mythostrike.model.game.activity.ActiveSkill;
import com.mythostrike.model.game.activity.PassiveSkill;
import com.mythostrike.model.game.activity.skill.active.SynergyOrder;
import com.mythostrike.model.game.activity.skill.active.Warrior;
import com.mythostrike.model.game.activity.skill.passive.DarkIllusion;
import com.mythostrike.model.game.activity.skill.passive.DeadlyCrusher;
import com.mythostrike.model.game.activity.skill.passive.DivineJustice;
import com.mythostrike.model.game.activity.skill.passive.EndlessHunger;
import com.mythostrike.model.game.activity.skill.passive.InfinityDance;
import com.mythostrike.model.game.activity.skill.passive.Revenge;
import com.mythostrike.model.game.activity.skill.passive.SongOfLullaby;
import com.mythostrike.model.game.activity.skill.passive.Strength;
import com.mythostrike.model.game.activity.skill.passive.VisionOfNight;
import lombok.Getter;

import java.util.List;

@Getter
public enum ChampionData {
    ARES("Ares", 4, List.of(new Warrior()), List.of(new Strength())),
    ACHILLES("Achilles", 4, List.of(), List.of(new Revenge())),
    HERACLES("Heracles", 4, List.of(), List.of(new DivineJustice())),
    TERPSICHORE("Terpsichore", 3, List.of(), List.of(new InfinityDance(), new SongOfLullaby())),
    KRATOS("Kratos", 5, List.of(), List.of(new DeadlyCrusher(), new EndlessHunger())),
    NYX("Nyx", 3, List.of(), List.of(new VisionOfNight(), new DarkIllusion())),
    HESTIA("Hestia", 4, List.of(new SynergyOrder()), List.of());

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
