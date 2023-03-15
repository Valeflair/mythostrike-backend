package com.mythostrike.model.game.player;

import com.mythostrike.model.game.activity.ActiveSkill;
import com.mythostrike.model.game.activity.PassiveSkill;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class Champion {

    private final int id;
    private final String name;
    private final int maxHp;
    private final List<PassiveSkill> passiveSkills;
    private final List<ActiveSkill> activeSkills;

    public Champion(int id, ChampionData data) {
        this.id = id;
        this.name = data.getName();
        this.maxHp = data.getMaxHp();
        this.passiveSkills = List.copyOf(data.getPassiveSkills());
        this.activeSkills = List.copyOf(data.getActiveSkills());

        for (int i = 0; i < activeSkills.size(); i++) {
            activeSkills.get(i).initialize(i);
        }
    }

    @Override
    public String toString() {
        return "Champion{" +
                "name='" + name + '\'' +
                '}';
    }
}
