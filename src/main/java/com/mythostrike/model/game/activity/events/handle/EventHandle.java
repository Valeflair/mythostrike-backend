package com.mythostrike.model.game.activity.events.handle;


import com.mythostrike.model.game.management.GameManager;
import com.mythostrike.model.game.player.Player;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class EventHandle {


    private GameManager gameManager;
    private String reason;
    private Player player;

    protected EventHandle(GameManager gameManager, String reason, Player player) {
        this.gameManager = gameManager;
        this.reason = reason;
        this.player = player;
    }


}

