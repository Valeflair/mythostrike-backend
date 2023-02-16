package com.mythostrike.model.lobby;


import com.fasterxml.jackson.annotation.JsonGetter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public record Mode(int id, String name, String description, int minPlayer, int maxPlayer, List<Identity> identityList) {

    public Mode(int id, ModeData data) {
        this(id, data.getName(), data.getDescription(), data.getMinPlayer(),
            data.getMaxPlayer(), data.getIdentityList());
    }

    public @UnmodifiableView List<Identity> getIdentityList() {
        return Collections.unmodifiableList(identityList);
    }

    @JsonGetter("identityList")
    private List<String> getIdentityListToString() {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Mode mode = (Mode) o;

        return id == mode.id;
    }

    public boolean isFrom(ModeData data) {
        return this.name.equals(data.getName());
    }
}
