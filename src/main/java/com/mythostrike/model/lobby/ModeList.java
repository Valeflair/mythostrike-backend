package com.mythostrike.model.lobby;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class ModeList {
    private static ModeList instance;
    private final List<Mode> modes;

    /**
     * This is a Singelton Class. The Constructor is private.
     * Use the getModeList() method to get the instance.
     * The ModeList is created from the Enum Values ModeData.
     */
    private ModeList() {
        this.modes = new ArrayList<>();
        for (ModeData modeData : ModeData.values()) {
            modes.add(new Mode(modeData.ordinal(), modeData));
        }
    }

    public static ModeList getModeList() {
        if (instance == null) {
            instance = new ModeList();
        }
        return instance;
    }

    public List<Mode> getAllModes() {
        return Collections.unmodifiableList(modes);
    }

    public Mode getMode(int id) {
        return modes.get(id);
    }
}
