package com.mythostrike.model.lobby;


import java.util.ArrayList;
import java.util.List;

public record Mode(int id, String name, String description, int minPlayer, int maxPlayer, List<Identity> identityList) {

    public Mode(int id, ModeData data) {
        this(id, data.getName(), data.getDescription(), data.getMinPlayer(),
            data.getMaxPlayer(), data.getIdentityList());
    }

    public List<String> getIdentityList() {
        List<String> result = new ArrayList<>();
        for (Identity identity : this.identityList) {
            //when the identity is incognito, the player shouldn't know about it for now.
            if (identity.isIncognito()) {
                result.add(Identity.NONE.toString());
            } else {
                result.add(identity.toString());
            }
        }
        return result;
    }
}
