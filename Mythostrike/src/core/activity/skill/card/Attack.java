package core.activity.skill.card;

import core.CardSymbol;
import core.CardType;
import core.Player;
import core.activity.Card;
import core.management.GameManager;
import skill.events.handle.*;
import skill.events.type.EventTypeAttack;

import java.util.ArrayList;
import java.util.List;

public class Attack extends Card {
    public static final String NAME = "Attack";
    public static final String DESCRIPTION = "pick a player as target, he has to play an \"Defend\" or get 1 damage.";
    public static final CardType TYPE = CardType.BASICCARD;

    private CardUseHandle handle;
    private List<Player> target;

    public Attack(CardSymbol symbol, int point) {
        super(NAME, DESCRIPTION, TYPE, symbol, point);
    }

    @Override
    public boolean checkCondition(CardUseHandle cardUseHandle) {
        Player player = cardUseHandle.getFrom();
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
        Player player = handle.getFrom();
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
            handle.setTo(players);
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
        List<Player> targets = handle.getTo();
        Player player = handle.getFrom();
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
                DamageHandle damageHandle = new DamageHandle(handle.getGameManager(), handle.getCard(), "attack damaged", player, target, 1 + attackHandle.getExtraDamage(), DamageType.NORMAL);
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