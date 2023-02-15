package com.mythostrike.controller.message.game;

import java.util.List;

public record UseSkillRequest(int lobbyId, int skillId, List<String> targets) {
}
