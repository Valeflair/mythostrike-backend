package com.mythostrike.model.game.activity.cards.cardtype;


import com.mythostrike.model.game.activity.cards.Card;
import com.mythostrike.model.game.activity.cards.CardSymbol;
import com.mythostrike.model.game.activity.cards.CardType;
import com.mythostrike.model.game.activity.events.handle.CardUseHandle;
import com.mythostrike.model.game.player.Player;

import java.util.List;

public class Defend extends Card {
    public static final String NAME = "Defend";
    public static final String DESCRIPTION = "Can be used to defend yourself. Can't be used actively.";
    public static final CardType TYPE = CardType.BASIC_CARD;

    private CardUseHandle handle;
    private List<Player> target;

    public Defend(int id, CardSymbol symbol, int point) {
        super(id, NAME, DESCRIPTION, TYPE, symbol, point);
    }

    @Override
    public Defend deepCopy() {
        return new Defend(id, symbol, point);
    }
}
