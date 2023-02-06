package com.mythostrike.model.game.core.activity.system.cardsystem;

import com.mythostrike.model.game.core.activity.Activity;
import com.mythostrike.model.game.core.activity.Card;
import com.mythostrike.model.game.core.activity.events.handle.AttackHandle;
import com.mythostrike.model.game.core.activity.events.handle.CardMoveHandle;
import com.mythostrike.model.game.core.activity.events.handle.DamageHandle;
import com.mythostrike.model.game.core.activity.events.handle.DamageType;
import com.mythostrike.model.game.core.activity.system.PickCardToPLay;
import com.mythostrike.model.game.core.activity.system.PickRequest;
import com.mythostrike.model.game.core.management.GameManager;
import com.mythostrike.model.game.core.player.Player;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

@Getter
@Setter
public class Attacked extends Activity {
    public static final String NAME = "Attacked";
    public static final String DESCRIPTION = "damage caused by attack ";
    public static final int ID = -11;

    private final GameManager gameManager;
    private AttackHandle attackHandle;
    private PickRequest pickRequest;

    public Attacked(GameManager gameManager) {
        super(ID, NAME, DESCRIPTION);
        this.gameManager = gameManager;
    }

    @Override
    public void use() {
        Player attacker = attackHandle.getPlayer();
        Player defender = attackHandle.getOpponent();
        if (pickRequest.getSelectedCards() == null || pickRequest.getSelectedCards().size() == 0) {
            Card attack = attackHandle.getAttack();
            new DamageHandle(gameManager, attack, DESCRIPTION, attacker, defender,
                attackHandle.getExtraDamage() + 1, DamageType.NORMAL);
            return;
        } else {
            CardMoveHandle cardMoveHandle = new CardMoveHandle(gameManager, "plays defend", defender, null,
                defender.getHandCards(), gameManager.getGame().getTablePile());
            cardMoveHandle.setMoveCards(pickRequest.getSelectedCards());
            gameManager.getCardManager().moveCard(cardMoveHandle);
        }
    }
}
