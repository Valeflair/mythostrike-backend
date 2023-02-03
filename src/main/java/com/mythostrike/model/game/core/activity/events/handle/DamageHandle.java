package com.mythostrike.model.game.core.activity.events.handle;


import com.mythostrike.model.game.core.player.Player;
import com.mythostrike.model.game.core.activity.Card;
import com.mythostrike.model.game.core.management.GameManager;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DamageHandle extends EventHandle {
    private Player to;
    private int damage;
    private boolean isPrevented;
    private DamageType damageType;
    private Card card;

    public DamageHandle(GameManager gameController, Card card, String reason, Player from, Player to, int damage,
                        DamageType damageType) {
        super(gameController, reason, from);
        this.to = to;
        this.damage = damage;
        this.isPrevented = false;
        this.damageType = damageType;
        this.card = card;
    }

    public DamageHandle(GameManager gameController, String reason, Player from, Player to) {
        super(gameController, reason, from);
        this.to = to;
        damage = 1;
        isPrevented = false;
        damageType = DamageType.NORMAL;
        card = null;
    }


}
