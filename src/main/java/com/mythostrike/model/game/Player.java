package com.mythostrike.model.game;

import com.mythostrike.account.repository.User;
import lombok.Getter;

@Getter
public class Player {

    private final String username;
    private final int avatarNumber;

    public Player(User user) {
        this.username = user.getUsername();
        this.avatarNumber = user.getAvatarNumber();
    }

    public Player(String username) {
        this.username = username;
        this.avatarNumber = 0;
    }

}
