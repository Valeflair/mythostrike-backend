package com.mythostrike.model.game.player;


import com.mythostrike.account.repository.User;
import com.mythostrike.model.game.activity.ActiveSkill;
import com.mythostrike.model.game.activity.PassiveSkill;
import com.mythostrike.model.game.activity.cards.CardSpaceRestrictedByName;
import com.mythostrike.model.game.activity.cards.CardSpaceRestrictedByType;
import com.mythostrike.model.game.activity.cards.CardType;
import com.mythostrike.model.game.activity.cards.HandCards;
import com.mythostrike.model.game.activity.cards.cardtype.Attack;
import com.mythostrike.model.game.activity.cards.cardtype.Drought;
import com.mythostrike.model.game.activity.cards.cardtype.Nightmare;
import com.mythostrike.model.game.management.GameManager;
import com.mythostrike.model.lobby.Identity;
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
    private static final Map<String, Integer> DEFAULT_RESTRICT = new HashMap<>();
    @Getter(AccessLevel.NONE)
    private static final Map<String, Boolean> DEFAULT_IMMUNITY = new HashMap<>();

    private final String username;
    private final HandCards handCards;
    private final CardSpaceRestrictedByType equipment;
    private final CardSpaceRestrictedByName delayedEffect;
    @Getter(AccessLevel.NONE)
    private final HashMap<String, Integer> permanentRestrict;
    @Getter(AccessLevel.NONE)
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
    @Getter(AccessLevel.NONE)
    private HashMap<String, Integer> restrict;
    @Getter(AccessLevel.NONE)
    private HashMap<String, Boolean> immunity;
    @Setter
    private boolean isAlive;
    private int avatarNumber;

    public Player(String username) {
        initilizeDefaultHashMaps();
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

    private void initilizeDefaultHashMaps() {
        DEFAULT_RESTRICT.put(Attack.NAME, 1);
    }

    public void initialize(GameManager gameManager) {
        //only the bot needs the gameManager
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
        Integer useTime = restrict.get(cardName);
        return useTime != null && useTime <= 0;
    }

    public boolean isImmune(String cardName) {
        Boolean isImmune = immunity.get(cardName);
        return isImmune != null && isImmune;
    }

    public void decreaseUseTime(String cardName) {
        Integer useTime = restrict.get(cardName);
        if (useTime != null && useTime > 0) {
            restrict.put(cardName, useTime - 1);
        }
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Player other && this.username.equals(other.username);
    }
}
