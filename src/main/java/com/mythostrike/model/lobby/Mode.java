package com.mythostrike.model.lobby;


import java.util.List;

public record Mode(int id, String name, String description, int minPlayer, int maxPlayer, List<Identity> identityList) {

    public Mode(int id, ModeData data) {
        this(id, data.getName(), data.getDescription(), data.getMinPlayer(),
            data.getMaxPlayer(), data.getIdentityList());
    }
}
