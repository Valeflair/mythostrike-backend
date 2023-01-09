package core;

import core.activity.ActiveSkill;
import core.activity.PassiveSkill;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Player {
    public static final HashMap<String, Integer> DEFAULT_RESTRICT = new HashMap<>();
    public static final HashMap<String, Boolean> DEFAULT_IMMUNITY = new HashMap<>();

    private String name;
    private Champion champion;
    private int currentHp;
    private int maxHp;
    private Identity identity;
    private CardSpace handCards;
    private CardSpace equipment;
    private CardSpace delayedEffect;
    private HashMap<String,Integer> permanentRestrict;
    private HashMap<String,Boolean> permanentImmunity;
    private HashMap<String,Integer> restrict;
    private HashMap<String,Boolean> immunity;
    private List<ActiveSkill> activeSkills;
    private List<PassiveSkill> passiveSkills;
    private boolean isAlive;

    public Player(String name) {
        permanentRestrict = new HashMap<String, Integer>(DEFAULT_RESTRICT);
        permanentImmunity = new HashMap<String, Boolean>(DEFAULT_IMMUNITY);
        resetRestrict();
        resetImmunity();
        handCards = new CardSpace();
        equipment = new CardSpace();
        delayedEffect = new CardSpace();
        isAlive = true;
        activeSkills = new ArrayList<>();
        passiveSkills = new ArrayList<>();
        this.name = name;
    }

    public void setChampion(Champion champion) {
        this.champion = champion;
    }

    public void increaseCurrentHp(int value) {
        currentHp += value;
    }
    public void decreaseCurrentHp(int value) {
        currentHp -= value;
    }
    public void resetRestrict() { restrict = new HashMap<>(permanentRestrict); }
    public void resetImmunity() { immunity = new HashMap<>(permanentImmunity); }
    public boolean isRestricted(String cardName) {
        return restrict.get(cardName) <= 0;
    }
    public boolean isImmune(String cardName) { return immunity.get(cardName); }
    public void decreaseUseTime(String cardName) { restrict.put(cardName, restrict.get(cardName) - 1); }


    public void setMaxHp(int maxHp) {
        this.maxHp = maxHp;
    }

    public void setIdentity(Identity identity) {
        this.identity = identity;
    }



    public String getName() {
        return name;
    }

    public Champion getChampion() {
        return champion;
    }

    public int getCurrentHp() {
        return currentHp;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public Identity getIdentity() {
        return identity;
    }

    public CardSpace getHandCards() {
        return handCards;
    }

    public CardSpace getEquipment() {
        return equipment;
    }

    public CardSpace getDelayedEffect() {
        return delayedEffect;
    }

    public List<ActiveSkill> getActiveSkills() {
        return activeSkills;
    }

    public List<PassiveSkill> getPassiveSkills() {
        return passiveSkills;
    }

    public boolean isAlive() {
        return isAlive;
    }
    public HashMap<String, Integer> getPermanentRestrict() {
        return permanentRestrict;
    }

    public HashMap<String, Boolean> getPermanentImmunity() {
        return permanentImmunity;
    }
}
