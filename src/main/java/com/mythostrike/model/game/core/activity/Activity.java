package com.mythostrike.model.game.core.activity;


import com.mythostrike.model.game.core.activity.events.handle.AttackHandle;
import com.mythostrike.model.game.core.activity.events.handle.CardAskHandle;
import com.mythostrike.model.game.core.activity.events.handle.CardDrawHandle;
import com.mythostrike.model.game.core.activity.events.handle.CardMoveHandle;
import com.mythostrike.model.game.core.activity.events.handle.CardUseHandle;
import com.mythostrike.model.game.core.activity.events.handle.DamageHandle;
import com.mythostrike.model.game.core.activity.events.handle.PhaseChangeHandle;
import com.mythostrike.model.game.core.activity.events.handle.PhaseHandle;
import com.mythostrike.model.game.core.activity.events.handle.PlayerHandle;
import lombok.Getter;

@Getter
public abstract class Activity {
    private final int id;
    private final String name;
    private final String description;


    protected Activity(int id, String name, String description) {
        this.id = id;
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
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        return obj instanceof Activity activity && name.equals(activity.getName());
    }
}
