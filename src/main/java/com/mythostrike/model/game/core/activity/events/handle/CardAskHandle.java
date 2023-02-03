package com.mythostrike.model.game.core.activity.events.handle;


import com.mythostrike.model.game.core.activity.cards.CardSpace;
import com.mythostrike.model.game.core.player.Player;
import com.mythostrike.model.game.core.activity.Card;
import com.mythostrike.model.game.core.management.GameManager;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CardAskHandle extends EventHandle {

    private List<String> requiredCardName;
    private List<Card> response;
    private CardSpace fromSpace;
    private CardSpace targetSpace;
    private int amount;
    private boolean responded;
    private boolean optional;

    public CardAskHandle(GameManager gameManager, String reason, Player player, List<String> requiredCardName,
                         CardSpace fromSpace, CardSpace targetSpace, int amount, boolean responded, boolean optional) {
        super(gameManager, reason, player);
        this.requiredCardName = requiredCardName;
        this.fromSpace = fromSpace;
        this.targetSpace = targetSpace;
        this.amount = amount;
        this.responded = responded;
        this.optional = optional;
        response = new ArrayList<>();
    }

    public boolean respondAble() {
        return filter().size() >= amount;
    }

    public List<Card> filter() {
        Player player = getPlayer();
        List<Card> response = new ArrayList<>();
        CardSpace cardSpace = fromSpace;
        for (Card card : cardSpace.getCards()) {
            // TODO consider View_As_Skill (Black as defend)
            String name = card.getName();
            if (requiredCardName.contains(name)) {
                response.add(card);
            }
        }
        return response;
    }

}
