package core.activity;


import core.Player;
import core.management.EventManager;


public abstract class PassiveSkill extends Activity {

    private Player player;

    public PassiveSkill(String name, String description, Player player) {
        super(name, description);
        this.player = player;
    }

    public Player getPlayer() { return player; }

    public void register(EventManager eventManager) { }
}
