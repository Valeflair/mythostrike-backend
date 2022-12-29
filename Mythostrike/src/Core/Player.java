package Core;

import java.util.ArrayList;
import java.util.HashMap;

public class Player {
    Champion champion;
    int currentHp;
    int maxHp;
    Identity identity;
    CardSpace handCards;
    CardSpace equipment;
    CardSpace delayedEffect;
    HashMap<CardData,Integer> restrict;
    HashMap<CardData,Boolean> immunity;
    ArrayList<Skill> skills;
    boolean isAlive;
    boolean isChained;

    public Player(Identity identity){
        this.identity = identity;
        restrict = new HashMap<CardData,Integer>();
        immunity = new HashMap<CardData,Boolean>();
        handCards = new CardSpace();
        equipment = new CardSpace();
        delayedEffect = new CardSpace();
        isAlive = true;
        isChained = false;
        skills = new ArrayList<Skill>();
    }
    public Champion getChampion() {
        return champion;
    }

    public void setChampion(Champion champion) {
        this.champion = champion;
    }

    public int getCurrentHp() {
        return currentHp;
    }

    public void setCurrentHp(int currentHp) {
        this.currentHp = currentHp;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public void setMaxHp(int maxHp) {
        this.maxHp = maxHp;
    }

    public Identity getIdentity() {
        return identity;
    }

    public void setIdentity(Identity identity) {
        this.identity = identity;
    }

    public CardSpace getHandCards() {
        return handCards;
    }

    public void setHandCards(CardSpace handCards) {
        this.handCards = handCards;
    }

    public CardSpace getEquipment() {
        return equipment;
    }

    public void setEquipment(CardSpace equipment) {
        this.equipment = equipment;
    }

    public CardSpace getDelayEffect() {
        return delayedEffect;
    }

    public void setDelayEffect(CardSpace delayedEffect) {
        this.delayedEffect = delayedEffect;
    }

    public HashMap<CardData, Integer> getRestrict() {
        return restrict;
    }

    public void setRestrict(HashMap<CardData, Integer> restrict) {
        this.restrict = restrict;
    }

    public HashMap<CardData, Boolean> getImmunity() {
        return immunity;
    }

    public void setImmunity(HashMap<CardData, Boolean> immunity) {
        this.immunity = immunity;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }

    public boolean isChained() {
        return isChained;
    }

    public void setChained(boolean chained) {
        isChained = chained;
    }

    public Skill hasSkillByName(String skillName){
        for (Skill skill:skills) {
            if (skill.equals(skillName)){
                return skill;
            }
        }
        return null;
    }
}
