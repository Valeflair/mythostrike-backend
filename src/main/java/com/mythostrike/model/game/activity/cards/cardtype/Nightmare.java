package com.mythostrike.model.game.activity.cards.cardtype;


import com.mythostrike.model.game.activity.Card;
import com.mythostrike.model.game.activity.cards.CardSymbol;
import com.mythostrike.model.game.activity.cards.CardType;
import com.mythostrike.model.game.activity.events.handle.CardUseHandle;
import com.mythostrike.model.game.player.Player;

import java.util.List;

public class Nightmare extends Card {
    public static final String NAME = "Nightmare";
    public static final String DESCRIPTION = "pick a player as target, he has to play an \"Defend\" or get 1 damage.";
    public static final CardType TYPE = CardType.BASICCARD;

    private CardUseHandle handle;
    private List<Player> target;

    public Nightmare(int id, CardSymbol symbol, int point) {
        super(id, NAME, DESCRIPTION, TYPE, symbol, point);
    }
    //TODO:implement
}
