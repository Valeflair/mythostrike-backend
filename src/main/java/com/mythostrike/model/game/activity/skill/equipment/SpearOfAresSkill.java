package com.mythostrike.model.game.activity.skill.equipment;

import com.mythostrike.controller.message.game.HighlightMessage;
import com.mythostrike.model.game.activity.PassiveSkill;
import com.mythostrike.model.game.activity.cards.Card;
import com.mythostrike.model.game.activity.cards.cardtype.Attack;
import com.mythostrike.model.game.activity.events.type.EventTypeRequest;
import com.mythostrike.model.game.activity.system.PickRequest;
import com.mythostrike.model.game.management.EventManager;
import com.mythostrike.model.game.player.Player;

import java.util.List;

public class SpearOfAresSkill extends PassiveSkill {

    public static final String NAME = "Spear of Ares Skill";
    public static final String DESCRIPTION = "If an Attack card is your last Handcard, "
        + "then this Attack can target up to 3 players";

    private PickRequest pickRequest;


    public SpearOfAresSkill() {
        super(NAME, DESCRIPTION);
    }

    @Override
    public void register(EventManager eventManager, Player player) {
        eventManager.registerEvent(EventTypeRequest.ACTIVE_TURN_REQUEST, this, player, false);
    }

    @Override
    public boolean checkCondition(PickRequest pickRequest) {
        this.pickRequest = pickRequest;
        HighlightMessage highlightMessage = pickRequest.getHighlightMessage();
        List<Card> cards = pickRequest.getGameManager().convertIdToCards(highlightMessage.cardIds());

        return cards.size() == 1 && cards.get(0).getName().equals(Attack.NAME);

    }

    @Override
    public void activate() {
        pickRequest.getHighlightMessage().cardPlayerConditions().get(0).count().add(2);
        pickRequest.getHighlightMessage().cardPlayerConditions().get(0).count().add(3);
    }
}
