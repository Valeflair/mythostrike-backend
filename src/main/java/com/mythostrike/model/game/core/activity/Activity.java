package com.mythostrike.model.game.core.activity;


import com.mythostrike.model.game.core.Player;
import com.mythostrike.model.game.skill.events.handle.AttackHandle;
import com.mythostrike.model.game.skill.events.handle.CardAskHandle;
import com.mythostrike.model.game.skill.events.handle.CardDrawHandle;
import com.mythostrike.model.game.skill.events.handle.CardMoveHandle;
import com.mythostrike.model.game.skill.events.handle.CardUseHandle;
import com.mythostrike.model.game.skill.events.handle.DamageHandle;
import com.mythostrike.model.game.skill.events.handle.PhaseChangeHandle;
import com.mythostrike.model.game.skill.events.handle.PhaseHandle;
import com.mythostrike.model.game.skill.events.handle.PlayerHandle;

import java.util.List;

public abstract class Activity {
    private String name;
    private String description;


    public Activity(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Activity activity)) {
            return false;
        }
        return name.equals(activity.getName());
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

    public void activate() {
    }

    public void pickedTargets(List<Player> players) {
    }

    /**
     * method for frontend after picked card
     *
     * @param cards cards picked
     */
    public void pickedCards(List<Card> cards) {
    }

    public void clickedConfirmButton(boolean confirm) {
    }

    public void use() {
    }


}
