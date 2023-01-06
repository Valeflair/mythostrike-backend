package core.game.management;

import core.game.Effect;
import core.skill.events.type.*;
import core.skill.events.handle.*;
import core.skill.events.handle.PhaseChangeHandle;

import java.util.ArrayList;
import java.util.HashMap;

public class EventManager {

    private GameManager gameManager;

    private HashMap<EventTypeDamage, ArrayList<Effect<DamageHandle>>> mapDamageHandle;
    private HashMap<EventTypePhase, ArrayList<Effect<PhaseHandle>>> mapPhaseHandle;
    private HashMap<EventTypePhaseChange, ArrayList<Effect<PhaseChangeHandle>>> mapPhaseChangeHandle;
    private HashMap<EventTypeCardUse, ArrayList<Effect<CardUseHandle>>> mapCardUseHandle;
    private HashMap<EventTypeCardDraw, ArrayList<Effect<CardDrawHandle>>> mapCardDrawHandle;
    private HashMap<EventTypeCardAsk, ArrayList<Effect<CardAskHandle>>> mapCardAskHandle;
    private HashMap<EventTypeCardMove, ArrayList<Effect<CardMoveHandle>>> mapCardMoveHandle;
    private HashMap<EventTypeAttack, ArrayList<Effect<AttackHandle>>> mapAttackHandle;

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
    public void registerEvent(EventTypeDamage eventType, Effect<DamageHandle> effect) {
        if (!mapDamageHandle.get(eventType).contains(effect)) {
            mapDamageHandle.get(eventType).add(effect);
        }
    }
    public void registerEvent(EventTypePhase eventType, Effect<PhaseHandle> effect) {
        if (!mapPhaseHandle.get(eventType).contains(effect)) {
            mapPhaseHandle.get(eventType).add(effect);
        }
    }
    public void registerEvent(EventTypePhaseChange eventType, Effect<PhaseChangeHandle> effect) {
        if (!mapPhaseChangeHandle.get(eventType).contains(effect)) {
            mapPhaseChangeHandle.get(eventType).add(effect);
        }
    }
    public void registerEvent(EventTypeCardUse eventType, Effect<CardUseHandle> effect) {
        if (!mapCardUseHandle.get(eventType).contains(effect)) {
            mapCardUseHandle.get(eventType).add(effect);
        }
    }
    public void registerEvent(EventTypeCardDraw eventType, Effect<CardDrawHandle> effect) {
        if (!mapCardDrawHandle.get(eventType).contains(effect)) {
            mapCardDrawHandle.get(eventType).add(effect);
        }
    }
    public void registerEvent(EventTypeCardAsk eventType, Effect<CardAskHandle> effect) {
        if (!mapCardAskHandle.get(eventType).contains(effect)) {
            mapCardAskHandle.get(eventType).add(effect);
        }
    }
    public void registerEvent(EventTypeCardMove eventType, Effect<CardMoveHandle> effect) {
        if (!mapCardMoveHandle.get(eventType).contains(effect)) {
            mapCardMoveHandle.get(eventType).add(effect);
        }
    }
    public void registerEvent(EventTypeAttack eventType, Effect<AttackHandle> effect) {
        if (!mapAttackHandle.get(eventType).contains(effect)) {
            mapAttackHandle.get(eventType).add(effect);
        }
    }

    public void triggerEvent(EventTypeDamage type, DamageHandle handle) {
        for (Effect<DamageHandle> effect : mapDamageHandle.get(type)) {
            if (effect.checkCondition(handle)) {
                effect.effect(handle);
            }
        }
    }
    public void triggerEvent(EventTypePhase type, PhaseHandle handle) {
        for (Effect<PhaseHandle> effect : mapPhaseHandle.get(type)) {
            if (effect.checkCondition(handle)) {
                effect.effect(handle);
            }
        }
    }
    public void triggerEvent(EventTypePhaseChange type, PhaseChangeHandle handle) {
        for (Effect<PhaseChangeHandle> effect : mapPhaseChangeHandle.get(type)) {
            if (effect.checkCondition(handle)) {
                effect.effect(handle);
            }
        }
    }
    public void triggerEvent(EventTypeCardUse type, CardUseHandle handle) {
        for (Effect<CardUseHandle> effect : mapCardUseHandle.get(type)) {
            if (effect.checkCondition(handle)) {
                effect.effect(handle);
            }
        }
    }
    public void triggerEvent(EventTypeCardDraw type, CardDrawHandle handle) {
        for (Effect<CardDrawHandle> effect : mapCardDrawHandle.get(type)) {
            if (effect.checkCondition(handle)) {
                effect.effect(handle);
            }
        }
    }
    public void triggerEvent(EventTypeCardAsk type, CardAskHandle handle) {
        for (Effect<CardAskHandle> effect : mapCardAskHandle.get(type)) {
            if (effect.checkCondition(handle)) {
                effect.effect(handle);
            }
        }
    }
    public void triggerEvent(EventTypeCardMove type, CardMoveHandle handle) {
        for (Effect<CardMoveHandle> effect : mapCardMoveHandle.get(type)) {
            if (effect.checkCondition(handle)) {
                effect.effect(handle);
            }
        }
    }
    public void triggerEvent(EventTypeAttack type, AttackHandle handle) {
        for (Effect<AttackHandle> effect : mapAttackHandle.get(type)) {
            if (effect.checkCondition(handle)) {
                effect.effect(handle);
            }
        }
    }



    public GameManager getGameManager() {
        return gameManager;
    }




}
