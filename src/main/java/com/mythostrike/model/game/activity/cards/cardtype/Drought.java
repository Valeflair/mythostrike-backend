package com.mythostrike.model.game.activity.cards.cardtype;


import com.mythostrike.controller.message.game.PlayerCondition;
import com.mythostrike.model.game.activity.cards.Card;
import com.mythostrike.model.game.activity.cards.CardSpace;
import com.mythostrike.model.game.activity.cards.CardSymbol;
import com.mythostrike.model.game.activity.cards.CardType;
import com.mythostrike.model.game.activity.events.handle.CardMoveHandle;
import com.mythostrike.model.game.activity.events.handle.CardUseHandle;
import com.mythostrike.model.game.management.GameManager;
import com.mythostrike.model.game.player.Player;

import java.util.ArrayList;
import java.util.List;

public class Drought extends Card {
    public static final String NAME = "Drought";
    public static final String DESCRIPTION = "pick a player as target, put drought under his delayed effect," +
            "at his judge turn he must judge, by not judging a Spade he will skip his Draw Turn";
    public static final CardType TYPE = CardType.SKILL_CARD;



    public Drought(int id, CardSymbol symbol, int point) {
        super(id, NAME, DESCRIPTION, TYPE, symbol, point);
    }

    @Override
    public boolean checkCondition(CardUseHandle cardUseHandle) {
        gameManager = cardUseHandle.getGameManager();
        Player player = cardUseHandle.getPlayer();
        List<Player> targets = new ArrayList<>();
        for (Player target : cardUseHandle.getGameManager().getGame().getOtherPlayers(player)) {
            if (!target.equals(player) && target.isAlive() && Boolean.FALSE.equals(target.isImmune(NAME))
                    && target.getDelayedEffect().accepts(this)) {
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
        cardMoveHandle = new CardMoveHandle(gameManager, "use card", cardUseHandle.getPlayer(),
                pickRequest.getSelectedPlayers().get(0),
                cardUseHandle.getPlayer().getHandCards(),
                pickRequest.getSelectedPlayers().get(0).getDelayedEffect(),
                List.of(this));
        playOut();
    }


    @Override
    public void use() {
        Card judge = gameManager.getCardManager().judge();
        if (judge.getSymbol().equals(CardSymbol.SPADE)) {
            String reason = "lucky, drought doesnt effect";
            //from is always the opponent player because Nightmare and Drought are initily played from the opponent
            Player from = pickRequest.getSelectedPlayers().get(0);
            Player to = null;
            CardSpace fromSpace = from.getDelayedEffect();
            CardSpace toSpace = gameManager.getGame().getTablePile();
            List<Card> cards = List.of(this);

            gameManager.getCardManager().moveCard(new CardMoveHandle(gameManager, reason, from, to,
                fromSpace, toSpace, cards));
        }
    }

    @Override
    public Drought deepCopy() {
        return new Drought(id, symbol, point);
    }


}
