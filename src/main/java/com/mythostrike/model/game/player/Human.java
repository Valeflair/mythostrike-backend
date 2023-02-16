package com.mythostrike.model.game.player;

import com.mythostrike.account.repository.User;
import com.mythostrike.account.service.UserService;
import lombok.Getter;

@Getter
public class Human extends Player {
    private final User user;
    private final UserService userService;

    public Human(User user, UserService userService) {
        super(user);
        this.user = user;
        this.userService = userService;
    }

    @Override
    public void addWinRewards() {
        user.changeDrachma(100);
        user.changeRankPoints(30);
        userService.saveUser(user);
    }

    @Override
    public void deductLoosePenalty() {
        int rankPointsToDeduct = Math.min(user.getRankPoints(), 15);
        user.changeRankPoints(rankPointsToDeduct);
        userService.saveUser(user);
    }

    @Override
    public int getDrachma() {
        return user.getDrachma();
    }

    @Override
    public int getRankPoints() {
        return user.getRankPoints();
    }
}
