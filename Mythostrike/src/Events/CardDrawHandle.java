package Events;

import Core.GameController;
import Core.Player;

public class CardDrawHandle extends EventHandle<CardDrawHandle> {
    Player player;
    int count;

    public CardDrawHandle(Player player, int count, GameController gameController) {
        this.player = player;
        this.count = count;
        super.setGameController(gameController);
    }
}
