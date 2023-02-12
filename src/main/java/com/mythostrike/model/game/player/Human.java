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
}
