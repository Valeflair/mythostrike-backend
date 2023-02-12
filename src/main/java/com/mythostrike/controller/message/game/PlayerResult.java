package com.mythostrike.controller.message.game;

import com.mythostrike.model.game.player.Human;
import com.mythostrike.model.game.player.Player;
import com.mythostrike.model.lobby.Identity;

public record PlayerResult(String username, Identity identity, int drachma, int rankPoints, boolean hasWon) {

    public PlayerResult(Human human, boolean hasWon) {
        this(human.getUsername(), human.getIdentity(), human.getUser().getDrachma(),
            human.getUser().getRankPoints(), hasWon);
    }

    /**
     * This constructor is used when the player is not a human, e.g. a bot.
     * The drachma and rankPoints will be set to 0.
     *
     * @param player the player
     * @param hasWon whether the player has won the game
     */
    public PlayerResult(Player player, boolean hasWon) {
        this(player.getUsername(), player.getIdentity(), 0, 0, hasWon);
    }
}
