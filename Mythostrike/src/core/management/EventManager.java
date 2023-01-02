package core.management;

import core.Game;
import core.Player;
import skill.events.Event;
import skill.events.EventType;
import skill.events.handle.*;
import skill.events.PhaseChangeHandle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EventManager {

    private GameManager gameManager;

    private Event<Game> gameStart;
    private Event<Player> turnStart;
    private Event<PhaseHandle> phaseStart;
    private Event<PhaseHandle> phaseProceeding;
    private Event<PhaseHandle> phaseEnd;
    private Event<PhaseChangeHandle> phaseChanging;
    private Event<PhaseChangeHandle> phaseSkipping;
    private Event<CardDrawHandle> drawCard;
    private Event<DamageHandle> beforeHpRecover;
    private Event<DamageHandle> afterHpRecover;
    private Event<DamageHandle> hpChanged;
    private Event<DamageHandle> confirmDamage;
    private Event<DamageHandle> damageForeseen;
    private Event<DamageHandle> damageCaused;
    private Event<DamageHandle> damageInflicted;
    private Event<DamageHandle> beforeDamageDone;
    private Event<DamageHandle> damageDone;
    private Event<DamageHandle> damage;
    private Event<DamageHandle> damaged;
    private Event<DamageHandle> damageComplete;
    private Event<AttackHandle> attackEffected;
    private Event<AttackHandle> attackProceed;
    private Event<AttackHandle> attackHit;
    private Event<AttackHandle> attackMissed;
    private Event<CardAskHandle> cardAsked;
    private Event<CardAskHandle> cardResponded;
    private Event<CardMoveHandle> beforeCardMove;
    private Event<CardMoveHandle> afterCardMove;
    private Event<CardUseHandle> beforeCardUse;
    private Event<CardUseHandle> cardUsed;
    private Event<CardUseHandle> targetConfirming;
    private Event<CardUseHandle> targetConfirmed;
    private List<Event<?>> events;



    public EventManager(GameManager gameManager){
        this.gameManager = gameManager;
        gameStart = new Event<>(EventType.GAME_START);
        turnStart = new Event<>(EventType.TURN_START);
        phaseStart = new Event<>(EventType.PHASE_START);
        phaseProceeding = new Event<>(EventType.PHASE_PROCEEDING);
        phaseEnd = new Event<>(EventType.PHASE_PROCEEDING);
        phaseChanging = new Event<>(EventType.PHASE_CHANGING);
        phaseSkipping = new Event<>(EventType.PHASE_SKIPPING);
        drawCard = new Event<>(EventType.DRAW_CARD);
        beforeHpRecover = new Event<>(EventType.BEFORE_HP_RECOVER);
        afterHpRecover = new Event<>(EventType.AFTER_HP_RECOVER);
        hpChanged = new Event<>(EventType.HP_CHANGED);
        confirmDamage = new Event<>(EventType.CONFIRM_DAMAGE);
        damageForeseen = new Event<>(EventType.DAMAGE_FORESEEN);
        damageCaused = new Event<>(EventType.DAMAGE_CAUSED);
        damageInflicted = new Event<>(EventType.DAMAGE_INFLICTED);
        beforeDamageDone = new Event<>(EventType.BEFORE_DAMAGE_DONE);
        damageDone = new Event<>(EventType.DAMAGE_DONE);
        damage = new Event<>(EventType.DAMAGE);
        damaged = new Event<>(EventType.DAMAGED);
        damageComplete = new Event<>(EventType.DAMAGE_COMPLETE);
        attackEffected = new Event<>(EventType.ATTACKEFFECTED);
        attackProceed = new Event<>(EventType.ATTACKPROCEED);
        attackHit = new Event<>(EventType.ATTACKHIT);
        attackMissed = new Event<>(EventType.ATTACKMISSED);
        cardAsked = new Event<>(EventType.CARD_ASKED);
        cardResponded = new Event<>(EventType.CARD_RESPONDED);
        beforeCardMove = new Event<>(EventType.BEFORE_CARD_MOVE);
        afterCardMove = new Event<>(EventType.AFTER_CARD_MOVE);
        beforeCardUse = new Event<>(EventType.BEFORE_CARD_USE);
        cardUsed = new Event<>(EventType.CARD_USED);
        targetConfirming = new Event<>(EventType.TARGET_CONFIRMING);
        targetConfirmed = new Event<>(EventType.TARGET_CONFIRMED);
        events = new ArrayList<>();
        events.add(gameStart);
        events.add(turnStart);
        events.add(phaseStart);
    }

    public GameManager getGameManager() {
        return gameManager;
    }

    public Event<Game> getGameStart() {
        return gameStart;
    }

    public Event<Player> getTurnStart() {
        return turnStart;
    }

    public Event<PhaseHandle> getPhaseStart() {
        return phaseStart;
    }

    public Event<PhaseHandle> getPhaseProceeding() {
        return phaseProceeding;
    }

    public Event<PhaseHandle> getPhaseEnd() {
        return phaseEnd;
    }

    public Event<PhaseChangeHandle> getPhaseChanging() {
        return phaseChanging;
    }

    public Event<PhaseChangeHandle> getPhaseSkipping() {
        return phaseSkipping;
    }

    public Event<CardDrawHandle> getDrawCard() {
        return drawCard;
    }

    public Event<DamageHandle> getBeforeHpRecover() {
        return beforeHpRecover;
    }

    public Event<DamageHandle> getAfterHpRecover() {
        return afterHpRecover;
    }

    public Event<DamageHandle> getHpChanged() {
        return hpChanged;
    }

    public Event<DamageHandle> getConfirmDamage() {
        return confirmDamage;
    }

    public Event<DamageHandle> getDamageForeseen() {
        return damageForeseen;
    }

    public Event<DamageHandle> getDamageCaused() {
        return damageCaused;
    }

    public Event<DamageHandle> getDamageInflicted() {
        return damageInflicted;
    }

    public Event<DamageHandle> getBeforeDamageDone() {
        return beforeDamageDone;
    }

    public Event<DamageHandle> getDamageDone() {
        return damageDone;
    }

    public Event<DamageHandle> getDamage() {
        return damage;
    }

    public Event<DamageHandle> getDamaged() {
        return damaged;
    }

    public Event<DamageHandle> getDamageComplete() {
        return damageComplete;
    }

    public Event<AttackHandle> getAttackEffected() {
        return attackEffected;
    }

    public Event<AttackHandle> getAttackProceed() {
        return attackProceed;
    }

    public Event<AttackHandle> getAttackHit() {
        return attackHit;
    }

    public Event<AttackHandle> getAttackMissed() {
        return attackMissed;
    }

    public Event<CardAskHandle> getCardAsked() {
        return cardAsked;
    }

    public Event<CardAskHandle> getCardResponded() {
        return cardResponded;
    }

    public Event<CardMoveHandle> getBeforeCardMove() {
        return beforeCardMove;
    }

    public Event<CardMoveHandle> getAfterCardMove() {
        return afterCardMove;
    }

    public Event<CardUseHandle> getBeforeCardUse() {
        return beforeCardUse;
    }

    public Event<CardUseHandle> getCardUsed() {
        return cardUsed;
    }

    public Event<CardUseHandle> getTargetConfirming() {
        return targetConfirming;
    }

    public Event<CardUseHandle> getTargetConfirmed() {
        return targetConfirmed;
    }

    public Event<?> getEvent(EventType type) {
        for (Event<?> event : events) {
            if (event.getType().equals(type)) {
                return event;
            }
        }
        return null;
    }

}
