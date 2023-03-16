package com.mythostrike.model.game.activity;

import com.mythostrike.model.game.player.Player;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
public class PassiveEffect {
    private PassiveSkill skill;
    private List<Player> temporaryPlayerList = new ArrayList<>();
    private List<Player> permanentPlayerList = new ArrayList<>();

    public PassiveEffect(PassiveSkill skill) {
        this.skill = skill;
    }

    public void cleanTemporaryPlayerList() {
        temporaryPlayerList.clear();
    }

    public void addPermanentTo(Player player) {
        if (!permanentPlayerList.contains(player)) {
            permanentPlayerList.add(player);
        }
    }

    public void addTemporaryTo(Player player) {
        if (!temporaryPlayerList.contains(player)) {
            temporaryPlayerList.add(player);
        }
    }

    public boolean deletePermanentFrom(Player player) {
        return permanentPlayerList.remove(player);
    }

    public boolean deleteTemporaryFrom(Player player) {
        return temporaryPlayerList.remove(player);
    }

    public boolean hasEffect(Player player) {
        return permanentPlayerList.contains(player) || temporaryPlayerList.contains(player);
    }

    @Override
    public boolean equals(Object object) {
        return object != null && object instanceof PassiveEffect other && skill.equals(other.getSkill());
    }

    @Override
    public int hashCode() {
        return Objects.hash(skill, temporaryPlayerList, permanentPlayerList);
    }
}

