package com.mythostrike.model.game.activity.cards.cardtype;


import com.mythostrike.controller.message.game.HighlightMessage;
import com.mythostrike.controller.message.game.PlayerCondition;
import com.mythostrike.model.game.activity.cards.Card;
import com.mythostrike.model.game.activity.cards.CardFilter;
import com.mythostrike.model.game.activity.cards.CardSymbol;
import com.mythostrike.model.game.activity.cards.CardType;
import com.mythostrike.model.game.activity.events.handle.AttackHandle;
import com.mythostrike.model.game.activity.events.handle.CardMoveHandle;
import com.mythostrike.model.game.activity.events.handle.CardUseHandle;
import com.mythostrike.model.game.activity.events.handle.DamageHandle;
import com.mythostrike.model.game.activity.events.handle.DamageType;
import com.mythostrike.model.game.activity.events.type.EventTypeAttack;
import com.mythostrike.model.game.activity.system.PickRequest;
import com.mythostrike.model.game.management.GameManager;
import com.mythostrike.model.game.player.Player;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Attack extends Card {
    public static final String NAME = Attack.class.getSimpleName();
    public static final String DESCRIPTION = "pick a player as target, he has to play an \"Defend\" or get 1 damage.";
    public static final CardType TYPE = CardType.BASIC_CARD;
    public static final CardFilter ATTACK_FILTER = new CardFilter(NAME);
    public static final CardFilter DEFEND_FILTER = new CardFilter(Defend.NAME);


    private AttackHandle attackHandle;

    private List<Player> targets;
    private boolean end;

    public Attack(int id, CardSymbol symbol, int point) {
        super(id, NAME, DESCRIPTION, TYPE, symbol, point);
    }

    /**
     * call in active turn, check if there is availble target to attack
     *
     * @param cardUseHandle the player uses this card
     * @return true if there is any target to attack
     */
    @Override
    public boolean checkCondition(CardUseHandle cardUseHandle) {
        gameManager = cardUseHandle.getGameManager();
        Player player = cardUseHandle.getPlayer();
        List<Player> targets = new ArrayList<>();
        for (Player target : cardUseHandle.getGameManager().getGame().getOtherPlayers(player)) {
            if (!target.equals(player) && target.isAlive() && Boolean.FALSE.equals(target.isImmune(NAME))) {
                targets.add(target);
            }
        }
        if (!targets.isEmpty() && !player.isRestricted(NAME)) {
            this.cardUseHandle = cardUseHandle;
            playerCondition = new PlayerCondition(GameManager.convertPlayersToUsername(targets), List.of(1));
            return true;
        }
        return false;
    }

    @Override
    public Attack deepCopy() {
        return new Attack(id, symbol, point);
    }

    /**
     * player choose this card, highlight all target enemies
     * creates a pickRequest for card user
     */
    @Override
    public void activate() {
        Player player = cardUseHandle.getPlayer();
        if (pickRequest.getSelectedPlayers() != null
            && !pickRequest.getSelectedPlayers().isEmpty()) {
            targets = pickRequest.getSelectedPlayers();
            cardMoveHandle = new CardMoveHandle(gameManager, "plays card", cardUseHandle.getPlayer(),
                null, player.getHandCards(), gameManager.getGame().getTablePile(),
                List.of(cardUseHandle.getCard()));
            cardUseHandle.setOpponents(targets);
            playOut();
            gameManager.queueActivity(this);
            attacksPlayer(targets.get(0));
            end = false;
        } else {
            end = true;
        }
    }

    /**
     * 2 cases:
     * case 1 : player picked target as enemy and play the card, create a pickRequest for the attacked enemy
     * case 2 : enemy picked card to play as defend, resolve the effect of card (damage or nothing)
     */
    @Override
    public void use() {
        Player player = cardUseHandle.getPlayer();

        if (targets == null || targets.isEmpty()) {
            end = true;
            return;
        }
        Player opponent = cardUseHandle.getOpponents().get(0);
        if (pickRequest.getSelectedCards() != null && !pickRequest.getSelectedCards().isEmpty()) {
            CardMoveHandle cardMoveHandle = new CardMoveHandle(gameManager, "plays card", opponent,
                null, opponent.getHandCards(), gameManager.getGame().getTablePile(),
                pickRequest.getSelectedCards());
            gameManager.getCardManager().moveCard(cardMoveHandle);
            attackHandle.setPrevented(true);
            gameManager.getEventManager().triggerEvent(EventTypeAttack.ATTACK_MISSED, attackHandle);
        } else {
            attackHandle.setPrevented(false);
            gameManager.getEventManager().triggerEvent(EventTypeAttack.ATTACK_HIT, attackHandle);
            DamageHandle damageHandle = new DamageHandle(cardUseHandle.getGameManager(), cardUseHandle.getCard(),
                "attack damaged", player, opponent, 1 + attackHandle.getExtraDamage(),
                DamageType.NORMAL);
            attackHandle.setDamageHandle(damageHandle);
            gameManager.getPlayerManager().applyDamage(damageHandle);
        }
        cardUseHandle.getOpponents().remove(0);
        if (targets != null && !targets.isEmpty()) {
            attacksPlayer(targets.get(0));
        }

    }

    @Override
    public boolean end() {
        return end;
    }

    public void attacksPlayer(Player opponent) {
        Player player = cardUseHandle.getPlayer();
        attackHandle = new AttackHandle(gameManager, cardUseHandle.getCard(), "", player, opponent);
        gameManager.getEventManager().triggerEvent(EventTypeAttack.ATTACK_EFFECTED, attackHandle);
        if (attackHandle.isPrevented()) {
            return;
        }
        gameManager.getEventManager().triggerEvent(EventTypeAttack.ATTACK_PROCEED, attackHandle);
        if (attackHandle.isPrevented()) {
            return;
        }
        List<Integer> cardIds =
            GameManager.convertCardsToInteger(DEFEND_FILTER.filter(opponent.getHandCards().getCards()));
        HighlightMessage highlightMessage = HighlightMessage.builder()
            .cardIds(cardIds)
            .cardCount(List.of(0, attackHandle.getDefendAskHandle().getAmount()))
            .reason(attackHandle.getDefendAskHandle().getReason())
            .build();
        pickRequest = new PickRequest(opponent, gameManager, highlightMessage);
        gameManager.queueActivity(pickRequest);
    }
}
