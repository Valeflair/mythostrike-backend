package Events;

import Core.GameController;

public abstract class EventHandle<T> {


    private GameController gameController;

    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }
    public GameController getGameController() {
        return gameController;
    }
}

