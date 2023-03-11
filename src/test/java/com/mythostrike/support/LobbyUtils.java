package com.mythostrike.support;

import com.mythostrike.model.game.player.Player;
import com.mythostrike.model.lobby.Identity;
import com.mythostrike.model.lobby.Mode;
import com.mythostrike.model.lobby.Seat;

import java.util.ArrayList;
import java.util.List;

public final class LobbyUtils {
    private LobbyUtils() {
    }

    public static List<Seat> createSeatList(List<Player> players, Mode mode) {
        List<Seat> seats = new ArrayList<>(mode.maxPlayer());
        List<Identity> identities = mode.identityList();
        for (int i = 0; i < mode.maxPlayer(); i++) {
            if (i > players.size() - 1) {
                seats.add(new Seat(i, null, identities.get(i)));
            } else {
                seats.add(new Seat(i, players.get(i), identities.get(i)));
            }
        }
        return seats;
    }
}
