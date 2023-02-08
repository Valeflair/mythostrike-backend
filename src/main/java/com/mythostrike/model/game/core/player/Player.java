package com.mythostrike.model.game.core.player;


import com.mythostrike.model.game.core.activity.ActiveSkill;
import com.mythostrike.model.game.core.activity.PassiveSkill;
import com.mythostrike.model.game.core.activity.cards.CardSpace;
import com.mythostrike.model.game.core.activity.cards.CardSpaceRestrictedByName;
import com.mythostrike.model.game.core.activity.cards.CardSpaceRestrictedByType;
import com.mythostrike.model.game.core.activity.cards.CardType;
import com.mythostrike.model.game.core.activity.cards.HandCards;
import com.mythostrike.model.game.core.activity.cards.cardtype.Drought;
import com.mythostrike.model.game.core.activity.cards.cardtype.Nightmare;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Player {
    public static final HashMap<String, Integer> DEFAULT_RESTRICT = new HashMap<>();
    public static final HashMap<String, Boolean> DEFAULT_IMMUNITY = new HashMap<>();
    @Getter
    private final String name;
    @Getter
    @Setter
    private Champion champion;
    @Getter
    private int currentHp;
    @Getter
    @Setter
    private int maxHp;
    @Getter
    @Setter
    private Identity identity;
    @Getter
    private final HandCards handCards;
    @Getter
    private final CardSpaceRestrictedByType equipment;
    @Getter
    private final CardSpaceRestrictedByName delayedEffect;
    @Getter
    private final HashMap<String, Integer> permanentRestrict;
    @Getter
    private final HashMap<String, Boolean> permanentImmunity;
    @Getter
    private HashMap<String, Integer> restrict;
    @Getter
    private HashMap<String, Boolean> immunity;
    @Getter
    private final List<ActiveSkill> activeSkills;
    @Getter
    private final List<PassiveSkill> passiveSkills;
    @Getter
    @Setter
    private boolean isAlive;

    public Player(String name) {
        permanentRestrict = new HashMap<String, Integer>(DEFAULT_RESTRICT);
        permanentImmunity = new HashMap<String, Boolean>(DEFAULT_IMMUNITY);
        resetRestrict();
        resetImmunity();

        isAlive = true;
        HashMap<CardType, Integer> equipmentCards = new HashMap<>();
        equipmentCards.put(CardType.WEAPON, 1);
        equipmentCards.put(CardType.ARMOR, 1);
        HashMap<String, Integer> delayedEffectCards = new HashMap<>();
        delayedEffectCards.put(Drought.NAME, 1);
        delayedEffectCards.put(Nightmare.NAME, 1);
        handCards = new HandCards();
        equipment = new CardSpaceRestrictedByType(equipmentCards);
        delayedEffect = new CardSpaceRestrictedByName(delayedEffectCards);
        activeSkills = new ArrayList<>();
        passiveSkills = new ArrayList<>();
        this.name = name;
    }

    public void increaseCurrentHp(int value) {
        currentHp += value;
    }

    public void decreaseCurrentHp(int value) {
        currentHp -= value;
    }

    public void resetRestrict() {
        restrict = new HashMap<>(permanentRestrict);
    }

    public void resetImmunity() {
        immunity = new HashMap<>(permanentImmunity);
    }

    public boolean isRestricted(String cardName) {
        return restrict.get(cardName) <= 0;
    }

    public boolean isImmune(String cardName) {
        return immunity.get(cardName);
    }

    public void decreaseUseTime(String cardName) {
        restrict.put(cardName, restrict.get(cardName) - 1);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Player other)) return false;
        return this.name.equals(other.name);
    }
}
