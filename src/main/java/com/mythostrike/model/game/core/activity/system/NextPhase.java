package com.mythostrike.model.game.core.activity.system;

import com.mythostrike.model.game.core.activity.SystemAction;
import com.mythostrike.model.game.core.activity.events.handle.PhaseChangeHandle;
import com.mythostrike.model.game.core.activity.events.type.EventTypePhaseChange;
import com.mythostrike.model.game.core.management.GameManager;
import com.mythostrike.model.game.core.Phase;
import lombok.Getter;

@Getter
public class NextPhase extends SystemAction {

    public static final String NAME = "NextPhase";
    public static final String DESCRIPTION = "goto Next Phase and put next Player if Phase end";
    public static final int ID = -1;

    private GameManager gameManager;

    //TODO : adjust with highlightMessage
    public NextPhase(GameManager gameManager) {
        super(ID, NAME, DESCRIPTION, gameManager);
    }

    @Override
    public void use() {
        Phase before = gameManager.getPhase();
        Phase after = Phase.nextPhase(before);
        PhaseChangeHandle phaseChangeHandle = new PhaseChangeHandle(
            gameManager, "switch phase", gameManager.getGame().getCurrentPlayer(), before, after);
        gameManager.getEventManager().triggerEvent(EventTypePhaseChange.PHASE_CHANGING, phaseChangeHandle);
        if (before.equals(Phase.FINISH)) {
            gameManager.getGame().nextPlayer();
        }
        gameManager.setPhase(after);
    }
}
