package com.mythostrike.model.game.activity;


import com.mythostrike.model.game.management.GameManager;

public class SystemAction extends Activity {

    protected final GameManager gameManager;

    public SystemAction(String name, String description, GameManager gameManager) {
        super(name, description);
        this.gameManager = gameManager;
    }
}
