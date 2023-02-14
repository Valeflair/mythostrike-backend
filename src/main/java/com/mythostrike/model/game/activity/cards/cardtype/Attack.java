package com.mythostrike.model.game.activity.cards.cardtype;


import com.mythostrike.controller.message.game.HighlightMessage;
import com.mythostrike.model.game.activity.Card;
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
    public static final CardFilter ATTACK_FILTER = new CardFilter("Attack");
    public static final CardFilter DEFEND_FILTER = new CardFilter("Defend");

    private CardUseHandle cardUseHandle;
    private PickRequest pickRequest;
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
        GameManager gameManager = cardUseHandle.getGameManager();
        //add targetAble enemy into targets
        List<Player> targets = new ArrayList<>();
        for (Player target : gameManager.getGame().getOtherPlayers(player)) {
            if (!target.equals(player) && target.isAlive() && Boolean.FALSE.equals(target.isImmune(NAME))) {
                targets.add(target);
            }
        }
        List<String> playerNames = GameManager.convertPlayersToUsername(targets);

        HighlightMessage highlightMessage = new HighlightMessage(null, playerNames, null, 0,
            0, 1, 1, DESCRIPTION, true, false, false);
        pickRequest = new PickRequest(player, gameManager, highlightMessage);
        gameManager.queueActivity(this);
        gameManager.queueActivity(pickRequest);
    }

    /**
     * 2 cases:
     * case 1 : player picked target as enemy and play the card, create a pickRequest for the attacked enemy
     * case 2 : enemy picked card to play as defend, resolve the effect of card (damage or nothing)
     */
    @Override
    public void use() {
        Player player = cardUseHandle.getPlayer();

        if (pickRequest.getPlayer().equals(cardUseHandle.getPlayer())) {
            if (pickRequest.isClickedCancel()) {
                end = true;
                return;
            }
            if (pickRequest.getHighlightMessage().minPlayer() > 0) {
                cardUseHandle.setOpponents(new ArrayList<>());
                //step 1 : chose target enemy
                if (pickRequest.getSelectedPlayers() != null
                        && !pickRequest.getSelectedPlayers().isEmpty()) {
                    targets = pickRequest.getSelectedPlayers();
                    pickRequest = new PickRequest(player, gameManager,
                            new HighlightMessage(null, null, null, 0, 0, 0, 0,
                                    "click confirm button to use attack", true, true, false));
                    gameManager.queueActivity(pickRequest);
                    end = false;
                } else {
                    end = true;
                    return;
                }
            } else {
                // step 2 : click confirm after chose

                cardMoveHandle = new CardMoveHandle(gameManager, "plays card", cardUseHandle.getPlayer(),
                        null, player.getHandCards(), gameManager.getGame().getTablePile(),
                        List.of(cardUseHandle.getCard()));

                playOut();
                cardUseHandle.setOpponents(targets);
                attacksPlayer(targets.get(0));

            }

        } else {
            if (targets == null || targets.isEmpty()) {
                end = true;
                return;
            }
            if (pickRequest.getPlayer().equals(cardUseHandle.getOpponents().get(0))) {
                Player opponent = cardUseHandle.getOpponents().get(0);
                if (pickRequest.getSelectedCards() != null) {
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
            }
        }
    }

    @Override
    public boolean end() {
        return end;
    }

    private void attacksPlayer(Player opponent) {
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
        List<Integer> cardIds = GameManager.convertCardsToInteger(DEFEND_FILTER.filter(opponent.getHandCards().getCards()));
        HighlightMessage highlightMessage = new HighlightMessage(cardIds, null, null,
            attackHandle.getDefendAskHandle().getAmount(), attackHandle.getDefendAskHandle().getAmount(),
            0, 0, attackHandle.getDefendAskHandle().getReason(),
            attackHandle.getDefendAskHandle().isOptional(), true, false);
        pickRequest = new PickRequest(opponent, gameManager, highlightMessage);
        gameManager.queueActivity(pickRequest);
    }
}
