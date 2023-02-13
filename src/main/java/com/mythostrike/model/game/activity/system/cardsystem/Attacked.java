package com.mythostrike.model.game.activity.system.cardsystem;

import com.mythostrike.model.game.activity.Activity;
import com.mythostrike.model.game.activity.Card;
import com.mythostrike.model.game.activity.events.handle.AttackHandle;
import com.mythostrike.model.game.activity.events.handle.CardMoveHandle;
import com.mythostrike.model.game.activity.events.handle.DamageHandle;
import com.mythostrike.model.game.activity.events.handle.DamageType;
import com.mythostrike.model.game.activity.system.PickRequest;
import com.mythostrike.model.game.management.GameManager;
import com.mythostrike.model.game.player.Player;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Attacked extends Activity {
    public static final String NAME = Attacked.class.getSimpleName();
    public static final String DESCRIPTION = "damage caused by attack ";

    private final GameManager gameManager;
    private AttackHandle attackHandle;
    private PickRequest pickRequest;

    public Attacked(GameManager gameManager) {
        super(NAME, DESCRIPTION);
        this.gameManager = gameManager;
    }

    @Override
    public void use() {
        Player attacker = attackHandle.getPlayer();
        Player defender = attackHandle.getOpponent();
        if (pickRequest.getSelectedCards() == null || pickRequest.getSelectedCards().isEmpty()) {
            Card attack = attackHandle.getAttack();
            new DamageHandle(gameManager, attack, DESCRIPTION, attacker, defender,
                attackHandle.getExtraDamage() + 1, DamageType.NORMAL);
        } else {
            CardMoveHandle cardMoveHandle = new CardMoveHandle(gameManager, "plays defend", defender, null,
                defender.getHandCards(), gameManager.getGame().getTablePile(), pickRequest.getSelectedCards());
            gameManager.getCardManager().moveCard(cardMoveHandle);
        }
    }
}
