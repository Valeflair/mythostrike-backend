package com.mythostrike.model.game.activity.events.handle;

import com.mythostrike.model.game.activity.cards.Card;
import com.mythostrike.model.game.activity.cards.CardFilter;
import com.mythostrike.model.game.management.GameManager;
import com.mythostrike.model.game.player.Player;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
public class CardFilterHandle extends EventHandle {

    @Getter
    CardFilter cardFilter;
    @Getter
    List<Card> cards;

    public CardFilterHandle(GameManager gameManager, Player player, CardFilter cardFilter, List<Card> cards) {
        super(gameManager, "filtering card", player);
        this.cardFilter = new CardFilter(cardFilter.toString());
        this.cards = cards;
    }
}
