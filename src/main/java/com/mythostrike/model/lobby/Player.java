package com.mythostrike.model.lobby;

import com.mythostrike.account.repository.User;
import lombok.Getter;

@Getter
public class Player {

    private String username;

    public Player(User user) {
        this.username = user.getUsername();
    }

    public Player(String username) {
        this.username = username;
    }

}