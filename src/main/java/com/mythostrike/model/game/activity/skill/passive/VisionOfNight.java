package com.mythostrike.model.game.activity.skill.passive;

import com.mythostrike.model.game.Game;
import com.mythostrike.model.game.Phase;
import com.mythostrike.model.game.activity.PassiveSkill;
import com.mythostrike.model.game.activity.cards.Card;
import com.mythostrike.model.game.activity.events.handle.CardMoveHandle;
import com.mythostrike.model.game.activity.events.handle.PhaseHandle;
import com.mythostrike.model.game.activity.events.type.EventTypePhase;
import com.mythostrike.model.game.management.EventManager;
import com.mythostrike.model.game.management.GameManager;
import com.mythostrike.model.game.player.Player;

import java.util.ArrayList;
import java.util.List;

public class VisionOfNight extends PassiveSkill {

    public static final String NAME = "Vision of Night";
    public static final String DESCRIPTION = "You can judge 2 cards at turn start, and get those black cards";
    PhaseHandle phaseHandle;
    public VisionOfNight() {
        super(NAME, DESCRIPTION);
    }

    @Override
    public void register(EventManager eventManager, Player player) {
        eventManager.registerEvent(EventTypePhase.PHASE_START, this, player, true);
    }

    @Override
    public boolean checkCondition(PhaseHandle phaseHandle) {
        if (phaseHandle.getPhase().equals(Phase.ROUND_START)
                && phaseHandle.getPlayer().getPassiveSkills().stream().anyMatch(passiveSkill -> passiveSkill.getName().equals(NAME))) {
            this.phaseHandle = phaseHandle;
            return true;
        }
        return false;
    }

    @Override
    public void activate() {
        GameManager gameManager = phaseHandle.getGameManager();
        for (int i = 0; i < 2; i++) {
            Card card = gameManager.getCardManager().judge();
            List<Card> cards = new ArrayList<>();
            if (!card.isRed()) {
                cards.add(card);
            }
            CardMoveHandle cardMoveHandle = new CardMoveHandle(gameManager, "get card by judging black",
                    null, phaseHandle.getPlayer(), gameManager.getGame().getDiscardPile(), phaseHandle.getPlayer().getHandCards(), cards);
            gameManager.getCardManager().moveCard(cardMoveHandle);
        }

    }
}
