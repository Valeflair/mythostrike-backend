package com.mythostrike.model.game.player;

import com.mythostrike.model.exception.IllegalInputException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class ChampionList {
    private static ChampionList instance;
    private final List<Champion> champions;

    /**
     * This is a Singleton Class. The Constructor is private.
     * Use the getChampionList() method to get the instance.
     * The ChampionList is created from the Enum Values ChampionData.
     */
    private ChampionList() {
        this.champions = new ArrayList<>();
        for (ChampionData data : ChampionData.values()) {
            champions.add(new Champion(data.ordinal(), data));
        }
    }

    public static ChampionList getChampionList() {
        if (instance == null) {
            instance = new ChampionList();
        }
        return instance;
    }

    public @NotNull @UnmodifiableView List<Champion> getChampions() {
        return Collections.unmodifiableList(champions);
    }

    public Champion getChampion(int id) throws IllegalInputException {
        if (id < 0 || id >= champions.size()) {
            throw new IllegalInputException(String.format("The id %d is not valid.", id));
        }
        return champions.get(id);
    }
}
