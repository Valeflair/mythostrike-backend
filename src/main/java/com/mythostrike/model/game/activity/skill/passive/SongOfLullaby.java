package com.mythostrike.model.game.activity.skill.passive;

import com.mythostrike.model.game.activity.PassiveSkill;
import com.mythostrike.model.game.activity.cards.cardtype.Nightmare;
import com.mythostrike.model.game.management.EventManager;
import com.mythostrike.model.game.player.Player;

public class SongOfLullaby extends PassiveSkill {

    public static final String NAME = "Song of Lullaby";
    public static final String DESCRIPTION = "You can't get targeted by Nightmare";


    public SongOfLullaby() {
        super(NAME, DESCRIPTION);
    }


    @Override
    public void register(EventManager eventManager, Player player) {
        player.setPermanentImmunity(Nightmare.NAME, true);
    }
}
