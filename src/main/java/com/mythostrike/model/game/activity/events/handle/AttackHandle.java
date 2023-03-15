package com.mythostrike.model.game.activity.events.handle;


import com.mythostrike.model.game.activity.cards.Card;
import com.mythostrike.model.game.activity.cards.cardtype.Defend;
import com.mythostrike.model.game.management.GameManager;
import com.mythostrike.model.game.player.Player;
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
        String message = "You got attacked by " + attacker.getUsername() + ". Choose a card to defend yourself with.";
        defendAskHandle = new CardAskHandle(gameManager, message, opponent, List.of(Defend.NAME),
            opponent.getHandCards(), gameManager.getGame().getTablePile(), 1, false, true);
        targetCount = 1;
    }

}
