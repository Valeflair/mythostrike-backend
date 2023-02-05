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
    private CardSpace fromSpace;
    private CardSpace toSpace;
    private List<Card> moveCards;

    public CardMoveHandle(GameManager gameController, String reason, Player player, Player to,
                          CardSpace fromSpace, CardSpace toSpace) {
        super(gameController, reason, player);
        this.to = to;
        this.fromSpace = fromSpace;
        this.toSpace = toSpace;
    }
}
