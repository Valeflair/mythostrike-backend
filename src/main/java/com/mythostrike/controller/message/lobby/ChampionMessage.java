package com.mythostrike.controller.message.lobby;

import com.mythostrike.model.game.player.Champion;

import java.util.List;

public record ChampionMessage(int id, String name, int maxHp, List<SkillMessage> passiveSkills,
                              List<SkillMessage> activeSkills) {

    public ChampionMessage(Champion champion) {
        this(champion.getId(), champion.getName(), champion.getMaxHp(),
            champion.getPassiveSkills().stream().map(SkillMessage::new).toList(),
            champion.getActiveSkills().stream().map(SkillMessage::new).toList());
    }
}
