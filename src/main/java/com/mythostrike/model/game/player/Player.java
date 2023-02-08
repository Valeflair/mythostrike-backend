package com.mythostrike.model.game.player;


import com.mythostrike.account.repository.User;
import com.mythostrike.model.game.activity.ActiveSkill;
import com.mythostrike.model.game.activity.PassiveSkill;
import com.mythostrike.model.game.activity.cards.CardSpaceRestrictedByName;
import com.mythostrike.model.game.activity.cards.CardSpaceRestrictedByType;
import com.mythostrike.model.game.activity.cards.CardType;
import com.mythostrike.model.game.activity.cards.HandCards;
import com.mythostrike.model.game.activity.cards.cardtype.Drought;
import com.mythostrike.model.game.activity.cards.cardtype.Nightmare;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Getter
public class Player {
    @Getter(AccessLevel.NONE)
    public static final Map<String, Integer> DEFAULT_RESTRICT = new HashMap<>();
    @Getter(AccessLevel.NONE)
    public static final Map<String, Boolean> DEFAULT_IMMUNITY = new HashMap<>();

    private final String username;
    private final HandCards handCards;
    private final CardSpaceRestrictedByType equipment;
    private final CardSpaceRestrictedByName delayedEffect;
    private final HashMap<String, Integer> permanentRestrict;
    private final HashMap<String, Boolean> permanentImmunity;
    private final List<ActiveSkill> activeSkills;
    private final List<PassiveSkill> passiveSkills;
    @Setter
    private Champion champion;
    private int currentHp;
    @Setter
    private int maxHp;
    @Setter
    private Identity identity;
    private HashMap<String, Integer> restrict;
    private HashMap<String, Boolean> immunity;
    @Setter
    private boolean isAlive;
    private int avatarNumber;

    public Player(String username) {
        permanentRestrict = new HashMap<>(DEFAULT_RESTRICT);
        permanentImmunity = new HashMap<>(DEFAULT_IMMUNITY);
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
        this.username = username;
        this.avatarNumber = 0;
    }

    public Player(User user) {
        this(user.getUsername());
        this.avatarNumber = user.getAvatarNumber();
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
        return obj instanceof Player other && this.username.equals(other.username);
    }
}
