package com.mythostrike.model.game.activity.system;

import com.mythostrike.model.game.Phase;
import com.mythostrike.model.game.activity.SystemAction;
import com.mythostrike.model.game.activity.cards.Card;
import com.mythostrike.model.game.activity.cards.cardtype.Drought;
import com.mythostrike.model.game.activity.cards.cardtype.Nightmare;
import com.mythostrike.model.game.activity.events.handle.CardMoveHandle;
import com.mythostrike.model.game.activity.events.handle.PhaseChangeHandle;
import com.mythostrike.model.game.activity.events.handle.PhaseHandle;
import com.mythostrike.model.game.activity.events.type.EventTypePhase;
import com.mythostrike.model.game.activity.events.type.EventTypePhaseChange;
import com.mythostrike.model.game.activity.system.phase.ActiveTurn;
import com.mythostrike.model.game.activity.system.phase.DelayedEffectTurn;
import com.mythostrike.model.game.activity.system.phase.DrawTurn;
import com.mythostrike.model.game.activity.system.phase.DropTurn;
import com.mythostrike.model.game.activity.system.phase.RoundStartTurn;
import com.mythostrike.model.game.management.GameManager;
import lombok.Getter;

import java.util.List;
import java.util.Optional;

@Getter
public class NextPhase extends SystemAction {

    public static final String NAME = NextPhase.class.getSimpleName();
    public static final String DESCRIPTION = "goto Next Phase and put next Player if Phase end";

    public NextPhase(GameManager gameManager) {
        super(NAME, DESCRIPTION, gameManager);
    }

    @Override
    public void use() {
        gameManager.cleanTable();
        Phase before = gameManager.getPhase();
        PhaseHandle phaseHandle = new PhaseHandle(gameManager, "switching phase", gameManager.getGame()
            .getCurrentPlayer(), before);
        gameManager.getEventManager().triggerEvent(EventTypePhase.PHASE_END, phaseHandle);
        Phase after = Phase.nextPhase(before);


        //Nightmare and Drought

        Optional<Card> firstDroughtCard = gameManager.getGame().getCurrentPlayer().getDelayedEffect().getCards().stream()
                .filter(card -> card.getName().equals(Drought.NAME))
                .findFirst();

        if (firstDroughtCard.isPresent() && after.equals(Phase.DRAW)) {
            Card drought = firstDroughtCard.get();
            gameManager.getCardManager().moveCard(new CardMoveHandle(gameManager, "drought effected!",
                    gameManager.getGame().getCurrentPlayer(), null,
                    gameManager.getGame().getCurrentPlayer().getDelayedEffect(),
                    gameManager.getGame().getTablePile(),
                    List.of(drought)));
            after = Phase.ACTIVE_TURN;
        }


        Optional<Card> firstNightmareCard = gameManager.getGame().getCurrentPlayer().getDelayedEffect().getCards().stream()
                .filter(card -> card.getName().equals(Nightmare.NAME))
                .findFirst();

        if (firstNightmareCard.isPresent() && after.equals(Phase.ACTIVE_TURN)) {
            Card nightmare = firstNightmareCard.get();
            gameManager.getCardManager().moveCard(new CardMoveHandle(gameManager, "nightmare effected!",
                    gameManager.getGame().getCurrentPlayer(), null,
                    gameManager.getGame().getCurrentPlayer().getDelayedEffect(),
                    gameManager.getGame().getTablePile(),
                    List.of(nightmare)));
            after = Phase.DISCARD;
        }







        PhaseHandle afterPhaseHandle = new PhaseHandle(gameManager, "switching phase", gameManager.getGame()
            .getCurrentPlayer(), after);


        PhaseChangeHandle phaseChangeHandle = new PhaseChangeHandle(
            gameManager, "switch phase", gameManager.getGame().getCurrentPlayer(), before, after);
        gameManager.getEventManager().triggerEvent(EventTypePhaseChange.PHASE_CHANGING, phaseChangeHandle);
        if (before.equals(Phase.FINISH)) {
            gameManager.getGame().nextPlayer();
        }
        switch (after) {
            case ROUND_START -> gameManager.queueActivity(new RoundStartTurn(gameManager));
            case DELAYED_EFFECT -> gameManager.queueActivity(new DelayedEffectTurn(gameManager));
            case DRAW -> gameManager.queueActivity(new DrawTurn(gameManager));
            case ACTIVE_TURN -> gameManager.queueActivity(new ActiveTurn(gameManager));
            case DISCARD -> gameManager.queueActivity(new DropTurn(gameManager));
            default -> {
            }
        }






        gameManager.getEventManager().triggerEvent(EventTypePhase.PHASE_START, afterPhaseHandle);
        gameManager.getEventManager().triggerEvent(EventTypePhase.PHASE_PROCEEDING, afterPhaseHandle);
        gameManager.setPhase(after);
    }
}
