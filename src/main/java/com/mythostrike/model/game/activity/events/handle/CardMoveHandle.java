package com.mythostrike.model.game.activity.events.handle;


import com.mythostrike.model.game.activity.Card;
import com.mythostrike.model.game.activity.cards.CardSpace;
import com.mythostrike.model.game.management.GameManager;
import com.mythostrike.model.game.player.Player;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CardMoveHandle extends EventHandle {
    private Player to;
    private CardSpace fromSpace;
    private CardSpace toSpace;
    private List<Card> cardsToMove;

    public CardMoveHandle(GameManager gameController, String reason, Player player, Player to,
                          CardSpace fromSpace, CardSpace toSpace, List<Card> cardsToMove) {
        super(gameController, reason, player);
        this.to = to;
        this.fromSpace = fromSpace;
        this.toSpace = toSpace;
        this.cardsToMove = new ArrayList<>(cardsToMove);
    }
}
