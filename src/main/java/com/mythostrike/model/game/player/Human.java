package com.mythostrike.model.game.player;

import com.mythostrike.account.repository.User;
import lombok.Getter;

@Getter
public class Human extends Player {
    private final User user;

    public Human(User user) {
        super(user.getUsername());
        this.user = user;
    }

    @Override
    public void addWinRewards() {
        user.changeDrachma(100);
        user.changeRankPoints(30);
    }

    @Override
    public void deductLoosePenalty() {
        int rankPointsToDeduct = Math.min(user.getRankPoints(), 15);
        user.changeRankPoints(rankPointsToDeduct);
    }
}
