package core;

import core.management.GameManager;
import skill.events.handle.*;
import skill.events.type.EventTypeAttack;

import java.util.ArrayList;
import java.util.function.Function;


public enum CardData {
    ATTACK("Attack", "pick a player, he has to play defend or get 1 damage", CardType.BASICCARD, new Effect<>(new Function<CardUseHandle, Boolean>() {
        @Override
        public Boolean apply(CardUseHandle handle) {
            Player player = handle.getFrom();
            ArrayList<Player> targets = new ArrayList<>();
            for (Player target : handle.getGameManager().getGame().getOtherPlayers(player)) {
                if (!target.equals(player) && target.isAlive && Boolean.FALSE.equals(target.getImmunity().get(ATTACK))) {
                    targets.add(target);
                }
            }

            return !targets.isEmpty() && player.getRestrict().get(ATTACK) > 0;
        }
    }, new Function<CardUseHandle, Boolean>() {
        @Override
        public Boolean apply(CardUseHandle cardUseHandle) {
            Player player = cardUseHandle.getFrom();
            GameManager gameManager = cardUseHandle.getGameManager();
            //add targetAble enemy into targets
            ArrayList<Player> targets = new ArrayList<>();
            for (Player target : gameManager.getGame().getOtherPlayers(player)) {
                if (!target.equals(player) && target.isAlive && Boolean.FALSE.equals(target.getImmunity().get(ATTACK))) {
                    targets.add(target);
                }
            }
            //ask player to pick a target from Attack
            ArrayList<Player> pickTarget = gameManager.getGameController().askForChosePlayer(player, targets, 1, 1, true, "pick a player to attack");
            if (pickTarget.isEmpty()) {
                return false;
            }
            if (!gameManager.getGameController().askForConfirm(player, "Confirm your Attack")) {
                return false;
            }
            cardUseHandle.setCardUseConfirmed(true);
            for (Player target : pickTarget) {
                AttackHandle attackHandle = new AttackHandle(gameManager, cardUseHandle.getCard(), "", player, target, null, 0);
                gameManager.getEventManager().triggerEvent(EventTypeAttack.ATTACK_EFFECTED, attackHandle);
                if (!attackHandle.isPrevented()) {
                    gameManager.getEventManager().triggerEvent(EventTypeAttack.ATTACK_HIT, attackHandle);
                    CardAskHandle cardAskHandle = attackHandle.getDefendAskHandle();
                    if (gameManager.getGameController().askForDiscard(cardAskHandle)) {
                        attackHandle.setPrevented(true);
                    } else {
                        attackHandle.setPrevented(false);
                        DamageHandle damageHandle = new DamageHandle(cardUseHandle.getGameManager(), cardUseHandle.getCard(), "attack damaged", player, target, 1 + attackHandle.getExtraDamage(), DamageType.NORMAL);
                        attackHandle.setDamageHandle(damageHandle);
                        gameManager.getPlayerManager().applyDamage(damageHandle);
                    }
                }
            }
            return true;
        }
    }))
    ,DEFEND("Defend", "use when you are getting Attack, prevent the damage of Attack", CardType.BASICCARD, new Effect<>(false, false));


    private final String name;
    private final String description;
    private final CardType type;
    private Effect<CardUseHandle> effect;

    CardData(String name, String description, CardType type, Effect<CardUseHandle> effect) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.effect = effect;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public CardType getType() {
        return type;
    }

    public boolean isPlayable(CardUseHandle cardUseHandle) {
        return effect.checkCondition(cardUseHandle);
    }

    public boolean apply(CardUseHandle cardUseHandle) {
        return effect.effect(cardUseHandle);
    }

}
