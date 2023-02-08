package com.mythostrike.model.game.activity.system;

import com.mythostrike.model.game.activity.events.handle.PhaseChangeHandle;
import com.mythostrike.model.game.activity.events.handle.PhaseHandle;
import com.mythostrike.model.game.activity.events.type.EventTypePhase;
import com.mythostrike.model.game.activity.events.type.EventTypePhaseChange;
import com.mythostrike.model.game.activity.system.phase.ActiveTurn;
import com.mythostrike.model.game.activity.system.phase.DelayedEffectTurn;
import com.mythostrike.model.game.activity.system.phase.DrawTurn;
import com.mythostrike.model.game.activity.system.phase.DropTurn;
import com.mythostrike.model.game.activity.system.phase.RoundStartTurn;
import com.mythostrike.model.game.Phase;
import com.mythostrike.model.game.activity.SystemAction;
import com.mythostrike.model.game.management.GameManager;
import lombok.Getter;

@Getter
public class NextPhase extends SystemAction {

    public static final String NAME = "NextPhase";
    public static final String DESCRIPTION = "goto Next Phase and put next Player if Phase end";
    public static final int ID = -1;

    private final GameManager gameManager;

    //TODO : adjust with highlightMessage
    public NextPhase(GameManager gameManager) {
        super(ID, NAME, DESCRIPTION, gameManager);
        this.gameManager = gameManager;
    }

    @Override
    public void use() {
        gameManager.cleanTable();
        Phase before = gameManager.getPhase();
        PhaseHandle phaseHandle = new PhaseHandle(gameManager, "switching phase", gameManager.getGame()
            .getCurrentPlayer(), before);
        gameManager.getEventManager().triggerEvent(EventTypePhase.PHASE_END, phaseHandle);
        Phase after = Phase.nextPhase(before);
        PhaseHandle afterPhaseHandle = new PhaseHandle(gameManager, "switching phase", gameManager.getGame()
            .getCurrentPlayer(), after);

        PhaseChangeHandle phaseChangeHandle = new PhaseChangeHandle(
            gameManager, "switch phase", gameManager.getGame().getCurrentPlayer(), before, after);
        gameManager.getEventManager().triggerEvent(EventTypePhaseChange.PHASE_CHANGING, phaseChangeHandle);
        if (before.equals(Phase.FINISH)) {
            gameManager.getGame().nextPlayer();
        }
        switch (after) {
            case ROUNDSTART -> gameManager.queueActivity(new RoundStartTurn(gameManager));
            case DELAYEDEFFECT -> gameManager.queueActivity(new DelayedEffectTurn(gameManager));
            case DRAW -> gameManager.queueActivity(new DrawTurn(gameManager));
            case ACTIVETURN -> gameManager.queueActivity(new ActiveTurn(gameManager));
            case DISCARD -> gameManager.queueActivity(new DropTurn(gameManager));
            default -> {
            }
        }
        gameManager.getEventManager().triggerEvent(EventTypePhase.PHASE_START, afterPhaseHandle);
        gameManager.getEventManager().triggerEvent(EventTypePhase.PHASE_PROCEEDING, afterPhaseHandle);
        gameManager.setPhase(after);
    }
}
