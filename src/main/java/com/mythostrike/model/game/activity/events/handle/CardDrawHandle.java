package com.mythostrike.model.game.activity.events.handle;


import com.mythostrike.model.game.activity.Card;
import com.mythostrike.model.game.activity.cards.CardPile;
import com.mythostrike.model.game.management.GameManager;
import com.mythostrike.model.game.player.Player;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CardDrawHandle extends EventHandle {
    private int count;
    private CardPile drawPile;
    private List<Card> drawCards;


    public CardDrawHandle(GameManager gameManager, String reason, Player player, int count,
                          CardPile drawPile) {
        super(gameManager, reason, player);
        this.count = count;
        this.drawPile = drawPile;
    }

}
