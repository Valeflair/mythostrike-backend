package com.mythostrike.model.game.core.activity.events.handle;


import com.mythostrike.model.game.core.activity.cards.CardPile;
import com.mythostrike.model.game.core.player.Player;
import com.mythostrike.model.game.core.activity.Card;
import com.mythostrike.model.game.core.management.GameManager;
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
