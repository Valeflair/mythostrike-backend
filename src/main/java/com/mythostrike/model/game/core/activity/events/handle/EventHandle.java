package com.mythostrike.model.game.core.activity.events.handle;


import com.mythostrike.model.game.core.player.Player;
import com.mythostrike.model.game.core.management.GameManager;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class EventHandle {


    private GameManager gameManager;
    private String reason;
    private Player player;

    public EventHandle(GameManager gameManager, String reason, Player player) {
        this.gameManager = gameManager;
        this.reason = reason;
        this.player = player;
    }


}

