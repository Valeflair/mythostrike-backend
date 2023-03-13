package com.mythostrike.controller.message.lobby;

import com.mythostrike.model.game.activity.Activity;

public record SkillMessage(int id, String name, String description) {
    public SkillMessage(Activity skill) {
        this(skill.getId(), skill.getName(), skill.getDescription());
    }
}
