package com.mythostrike.model.game.core.activity.events.handle;


import com.mythostrike.model.game.core.activity.cards.cardtype.Defend;
import com.mythostrike.model.game.core.player.Player;
import com.mythostrike.model.game.core.activity.Card;
import com.mythostrike.model.game.core.management.GameManager;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class AttackHandle extends EventHandle {

    private Player opponent;
    private Card defend;
    private Card attack;
    private int extraDamage;
    private boolean isPrevented;
    private CardAskHandle defendAskHandle;
    private int targetCount;
    private DamageHandle damageHandle;


    public AttackHandle(GameManager gameManager, Card attack, String reason, Player attacker, Player opponent) {
        super(gameManager, reason, attacker);
        this.opponent = opponent;
        this.defend = null;
        this.extraDamage = 0;
        this.attack = attack;
        isPrevented = false;
        defendAskHandle = new CardAskHandle(gameManager,  "you get attacked, drop a defend or you get damage",
            opponent, List.of(Defend.NAME), opponent.getHandCards(), gameManager.getGame().getTableDeck(), 1, false , true);
        targetCount = 1;
    }

}
