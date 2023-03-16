package com.mythostrike.model.game.activity.skill.passive;


import com.mythostrike.controller.message.game.HighlightMessage;
import com.mythostrike.model.game.activity.PassiveSkill;
import com.mythostrike.model.game.activity.cards.Card;
import com.mythostrike.model.game.activity.events.handle.CardMoveHandle;
import com.mythostrike.model.game.activity.events.handle.DamageHandle;
import com.mythostrike.model.game.activity.events.type.EventTypeDamage;
import com.mythostrike.model.game.activity.system.PickRequest;
import com.mythostrike.model.game.management.EventManager;
import com.mythostrike.model.game.management.GameManager;
import com.mythostrike.model.game.player.Player;

import java.util.ArrayList;
import java.util.List;

public class Revenge extends PassiveSkill {
    public static final String NAME = Revenge.class.getSimpleName();
    public static final String DESCRIPTION = "When you get damaged you can judge a card. "
        + "If it's red, the damage dealer looses 1 card. If it's black, you will revenge yourself "
        + "and deal 1 damage to the attacker.";
    private DamageHandle damageHandle;

    private PickRequest pickRequest;


    public Revenge() {
        super(NAME, DESCRIPTION);
    }


    @Override
    public void register(EventManager eventManager, Player player) {
        eventManager.registerEvent(EventTypeDamage.DAMAGED, this, player, true);
    }

    @Override
    public boolean checkCondition(DamageHandle damageHandle) {
        if (damageHandle.getTo().getPassiveSkills().stream()
            .anyMatch(passiveSkill -> passiveSkill.getName().equals(NAME))) {
            this.damageHandle = damageHandle;
            return true;
        }
        return false;
    }

    @Override
    public void activate() {
        Player player = damageHandle.getTo();
        GameManager gameManager = damageHandle.getGameManager();
        HighlightMessage highlightMessage = HighlightMessage.builder()
            .reason("Confirm to activate skill \"Revenge\".")
            .skillIds(List.of(id))
            .skillCount(List.of(0, 1))
            .skillPlayerConditions(List.of())
            .build();
        pickRequest = new PickRequest(damageHandle.getTo(), gameManager, highlightMessage);
        gameManager.queueActivity(this);
        gameManager.queueActivity(pickRequest);

    }

    @Override
    public void use() {
        if (pickRequest.isClickedCancel()) {
            return;
        }
        GameManager gameManager = damageHandle.getGameManager();
        Card card = gameManager.getCardManager().judge();
        gameManager.output("Revenge was activated! Judge result: " + card.getSymbol());
        if (card.isRed()) {
            if (damageHandle.getPlayer().getHandCards().getCards().isEmpty()) {
                return;
            }
            List<Card> throwCards = new ArrayList<>(damageHandle.getPlayer().getHandCards().getCards());
            Card throwCard = throwCards.get(gameManager.getRandom().nextInt(throwCards.size()));
            CardMoveHandle cardMoveHandle = new CardMoveHandle(gameManager, "drop because of Revenge",
                damageHandle.getPlayer(), null, damageHandle.getPlayer().getHandCards(),
                gameManager.getGame().getDiscardPile(), List.of(throwCard));
            gameManager.getCardManager().moveCard(cardMoveHandle);

        } else {
            DamageHandle damageHandleRevenge = new DamageHandle(gameManager, "damage by Revenge",
                damageHandle.getTo(), damageHandle.getPlayer());
            gameManager.getPlayerManager().applyDamage(damageHandleRevenge);
        }
    }

}
