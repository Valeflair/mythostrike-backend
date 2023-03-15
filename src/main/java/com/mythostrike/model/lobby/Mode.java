package com.mythostrike.model.lobby;


import org.jetbrains.annotations.UnmodifiableView;

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
