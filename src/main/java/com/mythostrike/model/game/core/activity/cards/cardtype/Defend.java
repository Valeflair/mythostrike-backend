package com.mythostrike.model.game.core.activity.cards.cardtype;


import com.mythostrike.model.game.core.activity.Card;
import com.mythostrike.model.game.core.activity.cards.CardSymbol;
import com.mythostrike.model.game.core.activity.cards.CardType;
import com.mythostrike.model.game.core.activity.events.handle.AttackHandle;
import com.mythostrike.model.game.core.activity.events.handle.CardAskHandle;
import com.mythostrike.model.game.core.activity.events.handle.CardUseHandle;
import com.mythostrike.model.game.core.activity.events.handle.DamageHandle;
import com.mythostrike.model.game.core.activity.events.handle.DamageType;
import com.mythostrike.model.game.core.activity.events.type.EventTypeAttack;
import com.mythostrike.model.game.core.management.GameManager;
import com.mythostrike.model.game.core.player.Player;

import java.util.ArrayList;
import java.util.List;

public class Defend extends Card {
    public static final String NAME = "Defend";
    public static final String DESCRIPTION = "pick a player as target, he has to play an \"Defend\" or get 1 damage.";
    public static final CardType TYPE = CardType.BASICCARD;

    private CardUseHandle handle;
    private List<Player> target;

    public Defend(int id, CardSymbol symbol, int point) {
        super(id, NAME, DESCRIPTION, TYPE, symbol, point);
    }
    //TODO:implement
}
