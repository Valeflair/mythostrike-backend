package com.mythostrike.model.game.activity.events.handle;

import com.mythostrike.model.game.activity.cards.Card;
import com.mythostrike.model.game.management.GameManager;
import com.mythostrike.model.game.player.Player;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CardUseHandle extends EventHandle {

    private List<Player> opponents;
    private Card card;

    public CardUseHandle(GameManager gameManager, Card card, String reason, Player player, List<Player> opponents,
                         boolean isHandCard) {
        super(gameManager, reason, player);
        this.opponents = opponents;
        this.card = card;
    }

    public CardUseHandle(GameManager gameManager, Card card, String reason, Player from, Player opponent,
                         boolean isHandCard) {
        super(gameManager, reason, from);
        this.opponents = new ArrayList<>();
        this.opponents.add(opponent);
        this.card = card;
    }

}
