package com.mythostrike.model.game.activity.cards.cardtype;


import com.mythostrike.controller.message.game.HighlightMessage;
import com.mythostrike.controller.message.game.PlayerCondition;
import com.mythostrike.model.game.activity.cards.Card;
import com.mythostrike.model.game.activity.cards.CardFilter;
import com.mythostrike.model.game.activity.cards.CardSymbol;
import com.mythostrike.model.game.activity.cards.CardType;
import com.mythostrike.model.game.activity.events.handle.CardMoveHandle;
import com.mythostrike.model.game.activity.events.handle.CardUseHandle;
import com.mythostrike.model.game.activity.events.handle.DamageHandle;
import com.mythostrike.model.game.activity.events.handle.DamageType;
import com.mythostrike.model.game.activity.system.PickRequest;
import com.mythostrike.model.game.management.GameManager;
import com.mythostrike.model.game.player.Player;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
/**
 * Attack card, player can choose a target to attack
 */
public class GodArena extends Card {

    public static final String NAME = GodArena.class.getSimpleName();
    public static final String DESCRIPTION = "use it and every other player has to play an \"Attack\" or get 1 damage.";
    public static final CardType TYPE = CardType.SKILL_CARD;
    public static final CardFilter ATTACK_FILTER = new CardFilter(Attack.NAME);

    private List<Player> targets;
    private boolean end;

    public GodArena(int id, CardSymbol symbol, int point) {
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
            cardUseHandle.setOpponents(targets);
            playerCondition = new PlayerCondition(new ArrayList<>(), List.of(0));
            return true;
        }
        return false;
    }

    @Override
    public GodArena deepCopy() {
        return new GodArena(id, symbol, point);
    }

    /**
     * player choose this card, highlight all target enemies
     * creates a pickRequest for card user
     */
    @Override
    public void activate() {
        Player player = cardUseHandle.getPlayer();
        targets = cardUseHandle.getOpponents();
        cardMoveHandle = new CardMoveHandle(gameManager, "plays card", cardUseHandle.getPlayer(),
                null, player.getHandCards(), gameManager.getGame().getTablePile(),
                List.of(cardUseHandle.getCard()));
        playOut();
        gameManager.queueActivity(this);
        attacksPlayer(targets.get(0));
        end = false;
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
        } else {
            DamageHandle damageHandle = new DamageHandle(cardUseHandle.getGameManager(), cardUseHandle.getCard(),
                "volcanic eruption damaged", player, opponent, 1,
                DamageType.NORMAL);
            gameManager.getPlayerManager().applyDamage(damageHandle);
        }
        cardUseHandle.getOpponents().remove(0);
        if (targets != null && !targets.isEmpty()) {
            attacksPlayer(targets.get(0));
        }


    }


    /**
     * check if the activity is finished
     * @return
     */
    @Override
    public boolean end() {
        return end;
    }

    public void attacksPlayer(Player opponent) {
        List<Integer> cardIds =
            GameManager.convertCardsToInteger(gameManager.getCardManager().filterCard(opponent.getHandCards().getCards(), ATTACK_FILTER, opponent));
        HighlightMessage highlightMessage = HighlightMessage.builder()
            .cardIds(cardIds)
            .cardCount(List.of(0, 1))
            .reason("you are under God Arena! drop a attack card or take 1 damage")
            .build();
        pickRequest = new PickRequest(opponent, gameManager, highlightMessage);
        gameManager.queueActivity(pickRequest);
    }
}
