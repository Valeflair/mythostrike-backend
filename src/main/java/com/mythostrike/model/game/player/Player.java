package com.mythostrike.model.game.player;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mythostrike.account.repository.User;
import com.mythostrike.model.game.activity.ActiveSkill;
import com.mythostrike.model.game.activity.PassiveSkill;
import com.mythostrike.model.game.activity.cards.CardSpaceRestrictedByName;
import com.mythostrike.model.game.activity.cards.CardSpaceRestrictedByType;
import com.mythostrike.model.game.activity.cards.CardSpaceType;
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
import java.util.Objects;


@Getter
public abstract class Player {
    @Getter(AccessLevel.NONE)
    private static final Map<String, Integer> DEFAULT_RESTRICT = new HashMap<>();
    @Getter(AccessLevel.NONE)
    private static final Map<String, Boolean> DEFAULT_IMMUNITY = new HashMap<>();

    private final String username;
    private final HandCards handCards;
    private final CardSpaceRestrictedByType equipment;
    private final CardSpaceRestrictedByName delayedEffect;
    @Getter(AccessLevel.NONE)
    private final Map<String, Integer> permanentRestrict;
    @Getter(AccessLevel.NONE)
    private final Map<String, Boolean> permanentImmunity;
    private final List<ActiveSkill> activeSkills;
    private final List<PassiveSkill> passiveSkills;
    @Setter
    private Champion champion;
    private int currentHp;
    private int maxHp;
    @Setter
    private Identity identity;
    @Getter(AccessLevel.NONE)
    private Map<String, Integer> restrict;
    @Getter(AccessLevel.NONE)
    private Map<String, Boolean> immunity;
    @Setter
    private boolean isAlive;
    private int avatarNumber;

    protected Player(String username) {
        initilizeDefaultHashMaps();
        permanentRestrict = new HashMap<>(DEFAULT_RESTRICT);
        permanentImmunity = new HashMap<>(DEFAULT_IMMUNITY);
        resetRestrict();
        resetImmunity();

        isAlive = true;
        Map<CardType, Integer> equipmentCards = new HashMap<>();
        equipmentCards.put(CardType.WEAPON, 1);
        equipmentCards.put(CardType.ARMOR, 1);
        Map<String, Integer> delayedEffectCards = new HashMap<>();
        delayedEffectCards.put(Drought.NAME, 1);
        delayedEffectCards.put(Nightmare.NAME, 1);
        handCards = new HandCards(username);
        equipment = new CardSpaceRestrictedByType(CardSpaceType.EQUIPMENT, username, equipmentCards);
        delayedEffect = new CardSpaceRestrictedByName(CardSpaceType.DELAYED_EFFECT, username, delayedEffectCards);
        activeSkills = new ArrayList<>();
        passiveSkills = new ArrayList<>();
        this.username = username;
        this.avatarNumber = 0;
    }

    protected Player(User user) {
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
        currentHp = Math.min(currentHp, maxHp);
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
        if (!restrict.containsKey(cardName)) {
            return false;
        }
        Integer useTime = restrict.get(cardName);
        return useTime != null && useTime <= 0;
    }

    public boolean isImmune(String cardName) {
        if (!immunity.containsKey(cardName)) {
            return false;
        }
        Boolean isImmune = immunity.get(cardName);
        return isImmune != null && isImmune;
    }

    public void decreaseUseTime(String cardName) {
        if (!restrict.containsKey(cardName)) {
            return;
        }
        Integer useTime = restrict.get(cardName);
        if (useTime != null && useTime > 0) {
            restrict.put(cardName, useTime - 1);
        }
    }

    public void increaseUseTime(String cardName) {
        if (!restrict.containsKey(cardName)) {
            return;
        }
        Integer useTime = restrict.get(cardName);
        if (useTime != null) {
            restrict.put(cardName, useTime + 1);
        }
    }

    public void setUseTime(String cardName, int count) {
        restrict.put(cardName, count);
    }

    public void setPermanentRestrict(String cardName, int count) {
        permanentRestrict.put(cardName, count);
    }

    public void setPermanentImmunity(String cardName, boolean immune) {
        permanentImmunity.put(cardName, immune);
    }

    public void setTemporaryImmunity(String cardName, boolean immune) {
        immunity.put(cardName, immune);
    }

    /**
     * @param cardName the name of the card
     * @return 1000 if the card is not restricted
     */
    public int getPermanentRestrict(String cardName) {
        if (!permanentRestrict.containsKey(cardName)) {
            return 1000;
        }
        return permanentRestrict.get(cardName);
    }

    /**
     * @param cardName the name of the card
     * @return false if the card is not immune
     */
    public boolean isPermanentImmune(String cardName) {
        return permanentImmunity.containsKey(cardName) && permanentImmunity.get(cardName);
    }

    public void addWinRewards() {
        //only the human player needs this
    }

    public void deductLoosePenalty() {
        //only the human player needs this
    }

    public void setMaxHp(int maxHp) {
        this.maxHp = maxHp;
        this.currentHp = Math.min(this.currentHp, maxHp);
    }

    @JsonIgnore
    public int getDrachma() {
        return 0;
    }

    @JsonIgnore
    public int getRankPoints() {
        return 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Player player = (Player) obj;

        return username.equals(player.username);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this);
    }
}
