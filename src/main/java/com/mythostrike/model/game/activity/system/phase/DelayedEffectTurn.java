package com.mythostrike.model.game.activity.system.phase;

import com.mythostrike.model.game.activity.Activity;
import com.mythostrike.model.game.activity.Card;
import com.mythostrike.model.game.management.GameManager;
import com.mythostrike.model.game.player.Player;

import java.util.List;

public class DelayedEffectTurn extends Activity {
    public static final String NAME = "DelayedEffect";
    public static final String DESCRIPTION = "counting it's delayed effect";
    public static final int ID = -11;

    private final GameManager gameManager;

    public DelayedEffectTurn(GameManager gameManager) {
        super(ID, NAME, DESCRIPTION);
        this.gameManager = gameManager;
    }

    @Override
    public void use() {
        Player player = gameManager.getGame().getCurrentPlayer();
        List<Card> delayedEffects = player.getDelayedEffect().getCards();
        for (Card card : delayedEffects) {
            gameManager.queueActivity(card);
        }
    }
}
