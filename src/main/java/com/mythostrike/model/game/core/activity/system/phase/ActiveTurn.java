package com.mythostrike.model.game.core.activity.system.phase;

import com.mythostrike.model.game.core.activity.Activity;
import com.mythostrike.model.game.core.activity.cards.CardSpace;
import com.mythostrike.model.game.core.activity.cards.HandCards;
import com.mythostrike.model.game.core.activity.events.handle.CardDrawHandle;
import com.mythostrike.model.game.core.activity.events.handle.PhaseHandle;
import com.mythostrike.model.game.core.activity.events.type.EventTypePhase;
import com.mythostrike.model.game.core.management.GameManager;
import com.mythostrike.model.game.core.player.Player;

public class ActiveTurn extends Activity {
    public static final String NAME = "Draw";
    public static final String DESCRIPTION = "Drawing it's card";
    public static final int ID = -11;
    public static final int CARD_COUNT_TURN_START = 2;

    private GameManager gameManager;

    public ActiveTurn(GameManager gameManager) {
        super(ID, NAME, DESCRIPTION);
    }

    @Override
    public void use() {
        Player player = gameManager.getGame().getCurrentPlayer();
        PhaseHandle phaseHandle = new PhaseHandle(gameManager, "running his phase", player, gameManager.getPhase());
        gameManager.getEventManager().triggerEvent(EventTypePhase.PHASE_START, phaseHandle);
        gameManager.getEventManager().triggerEvent(EventTypePhase.PHASE_PROCEEDING, phaseHandle);
        //TODO implement with highlightmessage

        HandCards handCard = player.getHandCards();



        gameManager.getEventManager().triggerEvent(EventTypePhase.PHASE_END, phaseHandle);
    }
}
