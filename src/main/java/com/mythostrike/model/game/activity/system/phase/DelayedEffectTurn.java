package com.mythostrike.model.game.activity.system.phase;

import com.mythostrike.model.game.activity.Activity;
import com.mythostrike.model.game.activity.cards.Card;
import com.mythostrike.model.game.management.GameManager;
import com.mythostrike.model.game.player.Player;

import java.util.List;

public class DelayedEffectTurn extends Activity {
    public static final String NAME = DelayedEffectTurn.class.getSimpleName();
    public static final String DESCRIPTION = "counting it's delayed effect";

    private final GameManager gameManager;

    public DelayedEffectTurn(GameManager gameManager) {
        super(NAME, DESCRIPTION);
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
