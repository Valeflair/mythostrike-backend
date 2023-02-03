package com.mythostrike.model.game.core.activity.events.handle;


import com.mythostrike.model.game.core.activity.cards.CardPile;
import com.mythostrike.model.game.core.activity.cards.CardSpace;
import com.mythostrike.model.game.core.player.Player;
import com.mythostrike.model.game.core.activity.Card;
import com.mythostrike.model.game.core.management.GameManager;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CardMoveHandle extends EventHandle {
    private Player to;
    private CardSpace fromPile;
    private CardPile toPile;
    private List<Card> moveCards;

    public CardMoveHandle(GameManager gameController, String reason, Player player, Player to,
                          CardPile fromPile, CardPile toPile) {
        super(gameController, reason, player);
        this.to = to;
        this.fromPile = fromPile;
        this.toPile = toPile;
    }
}
