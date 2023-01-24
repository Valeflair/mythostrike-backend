package com.mythostrike.model.game.skill.events.handle;


import com.mythostrike.model.game.core.Player;
import com.mythostrike.model.game.core.activity.Card;
import com.mythostrike.model.game.core.management.GameManager;

public abstract class EventHandle {


    private GameManager gameManager;
    private Card card;
    private String reason;
    private Player from;

    public EventHandle(GameManager gameManager, Card card, String reason, Player from) {
        this.gameManager = gameManager;
        this.card = card;
        this.reason = reason;
        this.from = from;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Player getFrom() {
        return from;
    }

    public void setFrom(Player from) {
        this.from = from;
    }

    public GameManager getGameManager() {
        return gameManager;
    }

    public void setGameManager(GameManager gameManager) {
        this.gameManager = gameManager;
    }


}

