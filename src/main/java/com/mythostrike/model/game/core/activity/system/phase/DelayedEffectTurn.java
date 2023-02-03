package com.mythostrike.model.game.core.activity.system.phase;

import com.mythostrike.model.game.core.activity.Activity;
import com.mythostrike.model.game.core.activity.Card;
import com.mythostrike.model.game.core.activity.events.handle.PhaseHandle;
import com.mythostrike.model.game.core.activity.events.type.EventTypePhase;
import com.mythostrike.model.game.core.management.GameManager;
import com.mythostrike.model.game.core.player.Player;

import java.util.List;

public class DelayedEffectTurn extends Activity {
    public static final String NAME = "DelayedEffect";
    public static final String DESCRIPTION = "counting it's delayed effect";
    public static final int ID = -11;

    private GameManager gameManager;

    public DelayedEffectTurn(GameManager gameManager) {
        super(ID, NAME, DESCRIPTION);
    }

    @Override
    public void use() {
        Player player = gameManager.getGame().getCurrentPlayer();
        PhaseHandle phaseHandle = new PhaseHandle(gameManager, "running his phase", player, gameManager.getPhase());
        gameManager.getEventManager().triggerEvent(EventTypePhase.PHASE_START, phaseHandle);
        gameManager.getEventManager().triggerEvent(EventTypePhase.PHASE_PROCEEDING, phaseHandle);
        List<Card> delayedEffects = player.getDelayedEffect().getCards();
        for (Card card :delayedEffects) {
            gameManager.getCurrentActivity().add(0, card);
        }
        gameManager.getEventManager().triggerEvent(EventTypePhase.PHASE_END, phaseHandle);
    }
}
