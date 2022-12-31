package core;

import java.util.ArrayList;
import java.util.HashMap;

public class Player {
    String name;
    Champion champion;
    int currentHp;
    int maxHp;
    Identity identity;
    Phase phase;
    CardSpace handCards;
    CardSpace equipment;
    CardSpace delayedEffect;
    HashMap<CardData,Integer> restrict;
    HashMap<CardData,Boolean> immunity;
    ArrayList<Skill> skills;
    boolean isAlive;
    boolean isChained;

    public Player(String name){
        restrict = new HashMap<CardData,Integer>();
        immunity = new HashMap<CardData,Boolean>();
        handCards = new CardSpace();
        equipment = new CardSpace();
        delayedEffect = new CardSpace();
        Card isNightmared;
        Card weapon;
        isAlive = true;
        isChained = false;
        skills = new ArrayList<Skill>();
        this.name = name;
        Phase phase = Phase.NOTACTIVE;
    }

    public String getName() {
        return name;
    }

    public Champion getChampion() {
        return champion;
    }

    public void initialChampions(Champion champion) {
        this.champion = champion;
        maxHp = champion.getMaxHp();
        skills.addAll(champion.getSkills());
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

    public ArrayList<Skill> getSkills() {
        return skills;
    }

    public CardSpace getDelayedEffect() {
        return delayedEffect;
    }

    public Skill hasSkillByName(String skillName){
        for (Skill skill:skills) {
            if (skill.equals(skillName)){
                return skill;
            }
        }
        return null;
    }
    public void setPhase(Phase phase){
        this.phase = phase;
    }
    public Phase getPhase() {
        return phase;
    }
}
