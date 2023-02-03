package com.mythostrike.model.game.core.activity.system;

import com.mythostrike.model.game.core.activity.Activity;
import com.mythostrike.model.game.core.activity.SystemAction;
import com.mythostrike.model.game.core.management.GameManager;
import lombok.Getter;

@Getter
public class Highlight extends SystemAction {

    public static final String NAME = "Highlight";
    public static final String DESCRIPTION = "highlight some cards, players and show hints";
    public static final int ID = -2;

    private GameManager gameManager;

    //TODO : adjust with highlightMessage

    public Highlight(GameManager gameManager) {
        super(ID, NAME, DESCRIPTION, gameManager);
    }
}
