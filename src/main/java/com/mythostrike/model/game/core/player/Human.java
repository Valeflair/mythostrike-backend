package com.mythostrike.model.game.core.player;

import com.mythostrike.account.repository.User;
import lombok.Getter;

@Getter
public class Human extends Player {
    User user;

    public Human(User user) {
        super(user.getUsername());
    }
}
