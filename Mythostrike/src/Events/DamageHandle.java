package Events;

import Core.Damage;
import Core.GameController;

public class DamageHandle {
    Damage damage;
    GameController gameController;

    public DamageHandle(Damage damage, GameController gameController) {
        this.damage = damage;
        this.gameController = gameController;
    }

    public Damage getDamage() {
        return damage;
    }

    public void setDamage(Damage damage) {
        this.damage = damage;
    }

    public GameController getGameController() {
        return gameController;
    }

    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }
}
