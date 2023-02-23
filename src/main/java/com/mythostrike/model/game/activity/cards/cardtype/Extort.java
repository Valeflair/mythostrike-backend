package com.mythostrike.model.game.activity.cards.cardtype;

import com.mythostrike.controller.message.game.PlayerCondition;
import com.mythostrike.model.game.activity.cards.Card;
import com.mythostrike.model.game.activity.cards.CardSymbol;
import com.mythostrike.model.game.activity.cards.CardType;
import com.mythostrike.model.game.activity.events.handle.CardMoveHandle;
import com.mythostrike.model.game.activity.events.handle.CardUseHandle;
import com.mythostrike.model.game.management.GameManager;
import com.mythostrike.model.game.player.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Extort extends Card {

    public static final String NAME = "Extort";
    public static final String DESCRIPTION = "pick a player as target, get a random card from him";
    public static final CardType TYPE = CardType.SKILL_CARD;


    public Extort(int id, CardSymbol symbol, int point) {
        super(id, NAME, DESCRIPTION, TYPE, symbol, point);
    }

    @Override
    public Extort deepCopy() {
        return new Extort(id, symbol, point);
    }

    @Override
    public boolean checkCondition(CardUseHandle cardUseHandle) {
        gameManager = cardUseHandle.getGameManager();
        Player player = cardUseHandle.getPlayer();
        List<Player> targets = new ArrayList<>();
        for (Player target : cardUseHandle.getGameManager().getGame().getOtherPlayers(player)) {
            if (!target.equals(player) && target.isAlive() && Boolean.FALSE.equals(target.isImmune(NAME))
                && target.getHandCards().size() > 0) {
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
    public void activate() {
        if (pickRequest.getSelectedPlayers().isEmpty()) {
            return;
        }

        cardMoveHandle = new CardMoveHandle(gameManager, "use card", cardUseHandle.getPlayer(),
            pickRequest.getSelectedPlayers().get(0),
            cardUseHandle.getPlayer().getHandCards(),
            gameManager.getGame().getTablePile(),
            List.of(this));
        playOut();
        Player player = cardUseHandle.getPlayer();
        for (Player target : pickRequest.getSelectedPlayers()) {
            List<Card> list = new ArrayList<>(target.getHandCards().getCards());
            Collections.shuffle(list);
            Card extort = list.get(0);

            gameManager.getCardManager().moveCard(
                new CardMoveHandle(gameManager, "extort", target, player, target.getHandCards(), player.getHandCards(),
                    List.of(extort)
                ));
        }


    }
}
