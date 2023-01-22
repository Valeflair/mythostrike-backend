package core.management;

import core.Effect;
import core.Game;
import core.Player;
import core.activity.PassiveSkill;
import skill.events.type.*;
import skill.events.handle.*;
import skill.events.handle.PhaseChangeHandle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;

public class EventManager {

    private GameManager gameManager;

    private HashMap<EventTypeDamage, List<PassiveSkill>> mapDamageHandle;
    private HashMap<EventTypePhase, List<PassiveSkill>> mapPhaseHandle;
    private HashMap<EventTypePhaseChange, List<PassiveSkill>> mapPhaseChangeHandle;
    private HashMap<EventTypeCardUse, List<PassiveSkill>> mapCardUseHandle;
    private HashMap<EventTypeCardDraw, List<PassiveSkill>> mapCardDrawHandle;
    private HashMap<EventTypeCardAsk, List<PassiveSkill>> mapCardAskHandle;
    private HashMap<EventTypeCardMove,List<PassiveSkill>> mapCardMoveHandle;
    private HashMap<EventTypeAttack, List<PassiveSkill>> mapAttackHandle;






    public EventManager(GameManager gameManager) {
        this.gameManager = gameManager;


        mapDamageHandle = new HashMap<>();
        for (EventTypeDamage eventType : EventTypeDamage.values()) {
            mapDamageHandle.put(eventType, new ArrayList<>());
        }
        mapPhaseHandle = new HashMap<>();
        for (EventTypePhase eventType : EventTypePhase.values()) {
            mapPhaseHandle.put(eventType, new ArrayList<>());
        }
        mapPhaseChangeHandle = new HashMap<>();
        for (EventTypePhaseChange eventType : EventTypePhaseChange.values()) {
            mapPhaseChangeHandle.put(eventType, new ArrayList<>());
        }
        mapCardUseHandle = new HashMap<>();
        for (EventTypeCardUse eventType : EventTypeCardUse.values()) {
            mapCardUseHandle.put(eventType, new ArrayList<>());
        }
        mapCardDrawHandle = new HashMap<>();
        for (EventTypeCardDraw eventType : EventTypeCardDraw.values()) {
            mapCardDrawHandle.put(eventType, new ArrayList<>());
        }
        mapCardAskHandle = new HashMap<>();
        for (EventTypeCardAsk eventType : EventTypeCardAsk.values()) {
            mapCardAskHandle.put(eventType, new ArrayList<>());
        }
        mapCardMoveHandle = new HashMap<>();
        for (EventTypeCardMove eventType : EventTypeCardMove.values()) {
            mapCardMoveHandle.put(eventType, new ArrayList<>());
        }
        mapAttackHandle = new HashMap<>();
        for (EventTypeAttack eventType : EventTypeAttack.values()) {
            mapAttackHandle.put(eventType, new ArrayList<>());
        }


    }

    public void registerEvent(EventTypeDamage type, PassiveSkill passiveSkill) {
        mapDamageHandle.get(type).add(passiveSkill);
    }
    public void registerEvent(EventTypePhase type, PassiveSkill passiveSkill) {
        mapPhaseHandle.get(type).add(passiveSkill);
    }
    public void registerEvent(EventTypePhaseChange type, PassiveSkill passiveSkill) {
        mapPhaseChangeHandle.get(type).add(passiveSkill);
    }
    public void registerEvent(EventTypeCardUse type, PassiveSkill passiveSkill) {
        mapCardUseHandle.get(type).add(passiveSkill);
    }
    public void registerEvent(EventTypeCardAsk type, PassiveSkill passiveSkill) {
        mapCardAskHandle.get(type).add(passiveSkill);
    }
    public void registerEvent(EventTypeCardMove type, PassiveSkill passiveSkill) {
        mapCardMoveHandle.get(type).add(passiveSkill);
    }
    public void registerEvent(EventTypeCardDraw type, PassiveSkill passiveSkill) {
        mapCardDrawHandle.get(type).add(passiveSkill);
    }
    public void registerEvent(EventTypeAttack type, PassiveSkill passiveSkill) {
        mapAttackHandle.get(type).add(passiveSkill);
    }



    public void triggerEvent(EventTypeDamage type, DamageHandle handle) {
        mapDamageHandle.get(type).forEach(PassiveSkill -> { if (PassiveSkill.checkCondition(handle)) { PassiveSkill.activate(); } });
    }
    public void triggerEvent(EventTypePhase type, PhaseHandle handle) {
        mapPhaseHandle.get(type).forEach(PassiveSkill -> { if (PassiveSkill.checkCondition(handle)) { PassiveSkill.activate(); } });
    }
    public void triggerEvent(EventTypePhaseChange type, PhaseChangeHandle handle) {
        mapPhaseChangeHandle.get(type).forEach(PassiveSkill -> { if (PassiveSkill.checkCondition(handle)) { PassiveSkill.activate(); } });
    }
    public void triggerEvent(EventTypeCardUse type, CardUseHandle handle) {
        mapCardUseHandle.get(type).forEach(PassiveSkill -> { if (PassiveSkill.checkCondition(handle)) { PassiveSkill.activate(); } });
    }
    public void triggerEvent(EventTypeCardDraw type, CardDrawHandle handle) {
        mapCardDrawHandle.get(type).forEach(PassiveSkill -> { if (PassiveSkill.checkCondition(handle)) { PassiveSkill.activate(); } });
    }
    public void triggerEvent(EventTypeCardAsk type, CardAskHandle handle) {
        mapCardAskHandle.get(type).forEach(PassiveSkill -> { if (PassiveSkill.checkCondition(handle)) { PassiveSkill.activate(); } });
    }
    public void triggerEvent(EventTypeCardMove type, CardMoveHandle handle) {
        mapCardMoveHandle.get(type).forEach(PassiveSkill -> { if (PassiveSkill.checkCondition(handle)) { PassiveSkill.activate(); } });
    }
    public void triggerEvent(EventTypeAttack type, AttackHandle handle) {
        mapAttackHandle.get(type).forEach(PassiveSkill -> { if (PassiveSkill.checkCondition(handle)) { PassiveSkill.activate(); } });
    }


    public GameManager getGameManager() {
        return gameManager;
    }




}
