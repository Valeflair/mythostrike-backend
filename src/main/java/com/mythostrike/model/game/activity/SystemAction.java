package com.mythostrike.model.game.activity;


import com.mythostrike.model.game.management.GameManager;

public class SystemAction extends Activity {

    GameManager gameManager;

    public SystemAction(int id, String name, String description, GameManager gameManager) {
        super(id, name, description);
        this.gameManager = gameManager;
    }
}
