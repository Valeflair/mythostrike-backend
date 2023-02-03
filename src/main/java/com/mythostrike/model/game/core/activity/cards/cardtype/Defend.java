package com.mythostrike.model.game.core.activity.cards.cardtype;


import com.mythostrike.model.game.core.activity.Card;
import com.mythostrike.model.game.core.activity.cards.CardSymbol;
import com.mythostrike.model.game.core.activity.cards.CardType;
import com.mythostrike.model.game.core.activity.events.handle.AttackHandle;
import com.mythostrike.model.game.core.activity.events.handle.CardAskHandle;
import com.mythostrike.model.game.core.activity.events.handle.CardUseHandle;
import com.mythostrike.model.game.core.activity.events.handle.DamageHandle;
import com.mythostrike.model.game.core.activity.events.handle.DamageType;
import com.mythostrike.model.game.core.activity.events.type.EventTypeAttack;
import com.mythostrike.model.game.core.management.GameManager;
import com.mythostrike.model.game.core.player.Player;

import java.util.ArrayList;
import java.util.List;

public class Defend extends Card {
    public static final String NAME = "Defend";
    public static final String DESCRIPTION = "pick a player as target, he has to play an \"Defend\" or get 1 damage.";
    public static final CardType TYPE = CardType.BASICCARD;

    private CardUseHandle handle;
    private List<Player> target;

    public Defend(CardSymbol symbol, int point) {
        super(NAME, DESCRIPTION, TYPE, symbol, point);
    }

    @Override
    public boolean checkCondition(CardUseHandle cardUseHandle) {
        Player player = cardUseHandle.getPlayer();
        ArrayList<Player> targets = new ArrayList<>();
        for (Player target : cardUseHandle.getGameManager().getGame().getOtherPlayers(player)) {
            if (!target.equals(player) && target.isAlive() && Boolean.FALSE.equals(target.isImmune(NAME))) {
                targets.add(target);
            }
        }
        if (!targets.isEmpty() && !player.isRestricted(NAME)) {
            handle = cardUseHandle;
            return true;
        }
        return false;
    }

    @Override
    public void activate() {
        Player player = handle.getPlayer();
        GameManager gameManager = handle.getGameManager();
        //add targetAble enemy into targets
        ArrayList<Player> targets = new ArrayList<>();
        for (Player target : gameManager.getGame().getOtherPlayers(player)) {
            if (!target.equals(player) && target.isAlive() && Boolean.FALSE.equals(target.isImmune(NAME))) {
                targets.add(target);
            }
        }
        //ArrayList<Player> pickTarget = gameManager.getGameController().askForChosePlayer(player, targets, 1, 1, true, "pick a player to attack");
    }

    @Override
    public void pickedTargets(List<Player> players) {
        if (players.isEmpty()) {
            handle.setOpponents(players);
            return;
        }
        //gameManager.getGameController().askForConfirm(player, "Confirm your Attack"
    }

    @Override
    public void clickedConfirmButton(boolean confirm) {
        if (confirm) {
            use();
        }
    }

    @Override
    public void use() {
        handle.setCardUseConfirmed(true);
        GameManager gameManager = handle.getGameManager();
        List<Player> targets = handle.getOpponents();
        Player player = handle.getPlayer();
        for (Player target : targets) {
            AttackHandle attackHandle = new AttackHandle(gameManager, handle.getCard(), "", player, target, 0);
            gameManager.getEventManager().triggerEvent(EventTypeAttack.ATTACK_EFFECTED, attackHandle);
            if (attackHandle.isPrevented()) {
                return;
            }
            gameManager.getEventManager().triggerEvent(EventTypeAttack.ATTACK_HIT, attackHandle);
            CardAskHandle cardAskHandle = attackHandle.getDefendAskHandle();
            if (gameManager.getGameController().askForDiscard(cardAskHandle)) {
                attackHandle.setPrevented(true);
            } else {
                attackHandle.setPrevented(false);
                DamageHandle damageHandle =
                    new DamageHandle(handle.getGameManager(), handle.getCard(), "attack damaged", player, target,
                        1 + attackHandle.getExtraDamage(), DamageType.NORMAL);
                attackHandle.setDamageHandle(damageHandle);
                gameManager.getPlayerManager().applyDamage(damageHandle);
            }
        }

    }

    @Override
    public void pickedCards(List<Card> cards) {
        super.pickedCards(cards);
    }
}
