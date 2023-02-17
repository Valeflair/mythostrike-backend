package com.mythostrike.model.game.management;

import com.mythostrike.model.game.activity.PassiveEffect;
import com.mythostrike.model.game.activity.PassiveSkill;
import com.mythostrike.model.game.activity.events.handle.AttackHandle;
import com.mythostrike.model.game.activity.events.handle.CardAskHandle;
import com.mythostrike.model.game.activity.events.handle.CardDrawHandle;
import com.mythostrike.model.game.activity.events.handle.CardMoveHandle;
import com.mythostrike.model.game.activity.events.handle.CardUseHandle;
import com.mythostrike.model.game.activity.events.handle.DamageHandle;
import com.mythostrike.model.game.activity.events.handle.PhaseChangeHandle;
import com.mythostrike.model.game.activity.events.handle.PhaseHandle;
import com.mythostrike.model.game.activity.events.type.*;
import com.mythostrike.model.game.activity.system.PickRequest;
import com.mythostrike.model.game.player.Player;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class EventManager {

    @Getter
    private final GameManager gameManager;

    private final HashMap<EventTypeDamage, List<PassiveEffect>> mapDamageHandle;
    private final HashMap<EventTypePhase, List<PassiveEffect>> mapPhaseHandle;
    private final HashMap<EventTypePhaseChange, List<PassiveEffect>> mapPhaseChangeHandle;
    private final HashMap<EventTypeCardUse, List<PassiveEffect>> mapCardUseHandle;
    private final HashMap<EventTypeCardDraw, List<PassiveEffect>> mapCardDrawHandle;
    private final HashMap<EventTypeCardAsk, List<PassiveEffect>> mapCardAskHandle;
    private final HashMap<EventTypeCardMove, List<PassiveEffect>> mapCardMoveHandle;
    private final HashMap<EventTypeAttack, List<PassiveEffect>> mapAttackHandle;
    private final HashMap<EventTypeRequest, List<PassiveEffect>> mapRequestHandle;


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
        mapRequestHandle = new HashMap<>();
        for (EventTypeRequest eventType : EventTypeRequest.values()) {
            mapRequestHandle.put(eventType, new ArrayList<>());
        }
    }


    public void addToMap(List<PassiveEffect> list, PassiveSkill passiveSkill, Player player, boolean permanent) {
        Optional<PassiveEffect> optionalEffect = list.stream()
                .filter(effect -> effect.getSkill().equals(passiveSkill))
                .findFirst();
        PassiveEffect effect;
        if (optionalEffect.isPresent()) {
            effect = optionalEffect.get();
        } else {
            effect = new PassiveEffect(passiveSkill);
            list.add(effect);
        }
        if (permanent) {
            effect.addPermanentTo(player);
        } else {
            effect.addTemporaryTo(player);
        }
    }

    public void cleanAllTemporary() {
        for (EventTypeDamage type : EventTypeDamage.values()) {
            mapDamageHandle.get(type).forEach(PassiveEffect::cleanTemporaryPlayerList);
        }
        for (EventTypePhase type : EventTypePhase.values()) {
            mapPhaseHandle.get(type).forEach(PassiveEffect::cleanTemporaryPlayerList);
        }
        for (EventTypePhaseChange type : EventTypePhaseChange.values()) {
            mapPhaseChangeHandle.get(type).forEach(PassiveEffect::cleanTemporaryPlayerList);
        }
        for (EventTypeCardUse type : EventTypeCardUse.values()) {
            mapCardUseHandle.get(type).forEach(PassiveEffect::cleanTemporaryPlayerList);
        }
        for (EventTypeCardDraw type : EventTypeCardDraw.values()) {
            mapCardDrawHandle.get(type).forEach(PassiveEffect::cleanTemporaryPlayerList);
        }
        for (EventTypeCardAsk type : EventTypeCardAsk.values()) {
            mapCardAskHandle.get(type).forEach(PassiveEffect::cleanTemporaryPlayerList);
        }
        for (EventTypeCardMove type : EventTypeCardMove.values()) {
            mapCardMoveHandle.get(type).forEach(PassiveEffect::cleanTemporaryPlayerList);
        }
        for (EventTypeAttack type : EventTypeAttack.values()) {
            mapAttackHandle.get(type).forEach(PassiveEffect::cleanTemporaryPlayerList);
        }
    }

    public void registerEvent(EventTypeDamage type, PassiveSkill passiveSkill, Player player, boolean permanent) {
        List<PassiveEffect> list = mapDamageHandle.get(type);
        addToMap(list, passiveSkill, player, permanent);
    }

    public void registerEvent(EventTypePhase type, PassiveSkill passiveSkill, Player player, boolean permanent) {
        List<PassiveEffect> list = mapPhaseHandle.get(type);
        addToMap(list, passiveSkill, player, permanent);
    }

    public void registerEvent(EventTypePhaseChange type, PassiveSkill passiveSkill, Player player, boolean permanent) {
        List<PassiveEffect> list = mapPhaseChangeHandle.get(type);
        addToMap(list, passiveSkill, player, permanent);
    }

    public void registerEvent(EventTypeCardUse type, PassiveSkill passiveSkill, Player player, boolean permanent) {
        List<PassiveEffect> list = mapCardUseHandle.get(type);
        addToMap(list, passiveSkill, player, permanent);
    }

    public void registerEvent(EventTypeCardDraw type, PassiveSkill passiveSkill, Player player, boolean permanent) {
        List<PassiveEffect> list = mapCardDrawHandle.get(type);
        addToMap(list, passiveSkill, player, permanent);
    }

    public void registerEvent(EventTypeCardAsk type, PassiveSkill passiveSkill, Player player, boolean permanent) {
        List<PassiveEffect> list = mapCardAskHandle.get(type);
        addToMap(list, passiveSkill, player, permanent);
    }

    public void registerEvent(EventTypeCardMove type, PassiveSkill passiveSkill, Player player, boolean permanent) {
        List<PassiveEffect> list = mapCardMoveHandle.get(type);
        addToMap(list, passiveSkill, player, permanent);
    }

    public void registerEvent(EventTypeAttack type, PassiveSkill passiveSkill, Player player, boolean permanent) {
        List<PassiveEffect> list = mapAttackHandle.get(type);
        addToMap(list, passiveSkill, player, permanent);
    }

    public void registerEvent(EventTypeRequest type, PassiveSkill passiveSkill, Player player, boolean permanent) {
        List<PassiveEffect> list = mapRequestHandle.get(type);
        addToMap(list, passiveSkill, player, permanent);
    }

    public void triggerEvent(EventTypeDamage type, DamageHandle handle) {
        mapDamageHandle.get(type).forEach(passiveEffect -> {
            if (passiveEffect.getSkill().checkCondition(handle)) {
                passiveEffect.getSkill().activate();
            }
        });
    }

    public void triggerEvent(EventTypePhase type, PhaseHandle handle) {
        mapPhaseHandle.get(type).forEach(passiveEffect -> {
            if (passiveEffect.getSkill().checkCondition(handle)) {
                passiveEffect.getSkill().activate();
            }
        });
    }

    public void triggerEvent(EventTypePhaseChange type, PhaseChangeHandle handle) {
        mapPhaseChangeHandle.get(type).forEach(passiveEffect -> {
            if (passiveEffect.getSkill().checkCondition(handle)) {
                passiveEffect.getSkill().activate();
            }
        });
    }

    public void triggerEvent(EventTypeCardUse type, CardUseHandle handle) {
        mapCardUseHandle.get(type).forEach(passiveEffect -> {
            if (passiveEffect.getSkill().checkCondition(handle)) {
                passiveEffect.getSkill().activate();
            }
        });
    }

    public void triggerEvent(EventTypeCardDraw type, CardDrawHandle handle) {
        mapCardDrawHandle.get(type).forEach(passiveEffect -> {
            if (passiveEffect.getSkill().checkCondition(handle)) {
                passiveEffect.getSkill().activate();
            }
        });
    }

    public void triggerEvent(EventTypeCardAsk type, CardAskHandle handle) {
        mapCardAskHandle.get(type).forEach(passiveEffect -> {
            if (passiveEffect.getSkill().checkCondition(handle)) {
                passiveEffect.getSkill().activate();
            }
        });
    }

    public void triggerEvent(EventTypeCardMove type, CardMoveHandle handle) {
        mapCardMoveHandle.get(type).forEach(passiveEffect -> {
            if (passiveEffect.getSkill().checkCondition(handle)) {
                passiveEffect.getSkill().activate();
            }
        });
    }

    public void triggerEvent(EventTypeAttack type, AttackHandle handle) {
        mapAttackHandle.get(type).forEach(passiveEffect -> {
            if (passiveEffect.getSkill().checkCondition(handle)) {
                passiveEffect.getSkill().activate();
            }
        });
    }
    public void triggerEvent(EventTypeRequest type, PickRequest handle) {
        mapRequestHandle.get(type).forEach(passiveEffect -> {
            if (passiveEffect.getSkill().checkCondition(handle)) {
                passiveEffect.getSkill().activate();
            }
        });
    }


}
