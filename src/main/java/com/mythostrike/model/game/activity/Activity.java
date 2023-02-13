package com.mythostrike.model.game.activity;


import com.mythostrike.model.game.activity.events.handle.AttackHandle;
import com.mythostrike.model.game.activity.events.handle.CardAskHandle;
import com.mythostrike.model.game.activity.events.handle.CardDrawHandle;
import com.mythostrike.model.game.activity.events.handle.CardMoveHandle;
import com.mythostrike.model.game.activity.events.handle.CardUseHandle;
import com.mythostrike.model.game.activity.events.handle.DamageHandle;
import com.mythostrike.model.game.activity.events.handle.PhaseChangeHandle;
import com.mythostrike.model.game.activity.events.handle.PhaseHandle;
import com.mythostrike.model.game.activity.events.handle.PlayerHandle;
import lombok.Getter;

import java.util.Objects;

@Getter
public abstract class Activity {
    private static int idCounter = 100;

    protected int id;
    protected final String name;
    protected final String description;


    protected Activity(String name, String description) {
        this.id = idCounter++;
        this.name = name;
        this.description = description;
    }


    public boolean checkCondition(AttackHandle attackHandle) {
        return false;
    }

    public boolean checkCondition(CardAskHandle cardAskHandle) {
        return false;
    }

    public boolean checkCondition(CardUseHandle cardUseHandle) {
        return false;
    }

    public boolean checkCondition(CardMoveHandle cardMoveHandle) {
        return false;
    }

    public boolean checkCondition(CardDrawHandle cardDrawHandle) {
        return false;
    }

    public boolean checkCondition(DamageHandle damageHandle) {
        return false;
    }

    public boolean checkCondition(PhaseChangeHandle phaseChangeHandle) {
        return false;
    }

    public boolean checkCondition(PhaseHandle phaseHandle) {
        return false;
    }

    public boolean checkCondition(PlayerHandle phaseHandle) {
        return false;
    }

    public void use() {
    }

    public void activate() {
    }

    /**
     * if returns true, Game Manager will delete this activity from queue after call use()
     *
     * @return value if the activity has to keep stay in currentActivity after use()
     */
    public boolean end() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Activity activity = (Activity) o;

        return id == activity.id && name.equals(activity.name) && description.equals(activity.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description);
    }
}
