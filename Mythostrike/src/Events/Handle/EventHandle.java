package Events.Handle;

import Core.Card;
import Core.GameController;
import Core.Player;

public abstract class EventHandle {


    private GameController gameController;

    private Card card;
    private String reason;
    private Player from;

    public EventHandle(GameController gameController, Card card, String reason, Player from) {
        this.gameController = gameController;
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

    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }
    public GameController getGameController() {
        return gameController;
    }


}

