package com.mythostrike.model.game.activity.skill.passive;

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
import java.util.Collections;
import java.util.List;

import static com.mythostrike.model.game.management.CardManager.JUDGE_PAUSE_ON_TABLE_PILE;
import static java.lang.Thread.sleep;

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
            && phaseHandle.getPlayer().getPassiveSkills().stream()
            .anyMatch(passiveSkill -> passiveSkill.getName().equals(NAME))) {
            this.phaseHandle = phaseHandle;
            return true;
        }
        return false;
    }

    @Override
    public void activate() {
        GameManager gameManager = phaseHandle.getGameManager();

        List<Card> cardsToJudge = Collections.unmodifiableList(gameManager.getCardManager().peekTopDrawPile(2));

        List<Card> blackCards = new ArrayList<>();
        List<Card> redCards = new ArrayList<>();
        for (Card card : cardsToJudge) {
            if (card.isRed()) {
                redCards.add(card);
            } else {
                blackCards.add(card);
            }
        }

        gameManager.getCardManager().moveCard(new CardMoveHandle(gameManager, "vision of night judge", null, null,
            gameManager.getGame().getDrawPile(), gameManager.getGame().getTablePile(), cardsToJudge));

        try {
            sleep(JUDGE_PAUSE_ON_TABLE_PILE);
        } catch (InterruptedException e) {
            //ignore
        }

        gameManager.getCardManager().moveCard(new CardMoveHandle(gameManager, "get card by judging black",
            null, phaseHandle.getPlayer(), gameManager.getGame().getTablePile(),
            phaseHandle.getPlayer().getHandCards(), blackCards));

        gameManager.getCardManager().moveCard(new CardMoveHandle(gameManager, "red card to discard pile",
            null, null, gameManager.getGame().getTablePile(),
            gameManager.getGame().getDiscardPile(), redCards));

    }
}
