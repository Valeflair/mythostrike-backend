package com.mythostrike.model.lobby;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.annotation.JsonValue;
import com.mythostrike.model.game.management.GameManager;
import com.mythostrike.model.game.player.Player;
import lombok.Getter;

import java.util.List;


@JsonFormat(shape = Shape.OBJECT)
public enum Identity {
    TEAM_RED("Team Red", false, false),
    TEAM_BLUE("Team Blue", false, false),
    GOD_KING("God King", false, true),
    GENERAL("General", true, false),
    REBEL("Rebel", true, false),
    RENEGADE("Renegade", true, true),
    NONE("None", false, true);

    static {
        TEAM_RED.hasToSurvive = List.of(TEAM_RED);
        TEAM_RED.hasToDie = List.of(TEAM_BLUE);

        TEAM_BLUE.hasToSurvive = List.of(TEAM_BLUE);
        TEAM_BLUE.hasToDie = List.of(TEAM_RED);

        GOD_KING.hasToSurvive = List.of(GOD_KING);
        GOD_KING.hasToDie = List.of(REBEL, RENEGADE);

        GENERAL.hasToSurvive = List.of(GOD_KING);
        GENERAL.hasToDie = List.of(REBEL, RENEGADE);

        REBEL.hasToSurvive = List.of(REBEL);
        REBEL.hasToDie = List.of(GOD_KING);

        RENEGADE.hasToSurvive = List.of(RENEGADE);
        RENEGADE.hasToDie = List.of(GOD_KING, GENERAL, REBEL);

        NONE.hasToDie = List.of(NONE);
    }

    @Getter
    @JsonValue
    @JsonUnwrapped
    private final String name;
    @Getter
    private final boolean incognito;
    private final boolean playerNeedsToBeAlive;
    private List<Identity> hasToSurvive;
    private List<Identity> hasToDie;

    Identity(String name, boolean incognito, boolean playerNeedsToBeAlive) {
        this.name = name;
        this.incognito = incognito;
        this.playerNeedsToBeAlive = playerNeedsToBeAlive;
    }

    public boolean hasWon(Player player , GameManager gameManager) {
        //check if player is alive if specified
        if (playerNeedsToBeAlive && !player.isAlive()) {
            return false;
        }
        //check if all identites that have to survive are alive
        if (hasToSurvive != null) {
            for (Identity identityToSurvive : hasToSurvive) {
                List<Player> alivePlayers = gameManager.getGame().getAlivePlayers();

                if (isIdentityDead(identityToSurvive, alivePlayers)) return false;
            }
        }
        //check if all identites that have to die are dead
        if (hasToDie != null) {
            for (Identity identityToDie : hasToDie) {
                List<Player> alivePlayers = gameManager.getGame().getAlivePlayers();

                if (isIdentityAlive(player, identityToDie, alivePlayers)) return false;
            }
        }
        return true;
    }

    private static boolean isIdentityAlive(Player player, Identity identityToDie, List<Player> alivePlayers) {
        for (Player alivePlayer : alivePlayers) {
            //check if alivePlayer is not the player himself, because sometimes players also have to kill
            //everyone with the same identity e.g. Free for all with Identity NONE
            if (!alivePlayer.equals(player) && alivePlayer.getIdentity().equals(identityToDie)) {
                return true;
            }
        }
        return false;
    }

    private static boolean isIdentityDead(Identity identityToSurvive, List<Player> alivePlayers) {
        return alivePlayers.stream().noneMatch(
            alivePlayer -> alivePlayer.getIdentity().equals(identityToSurvive)
        );
    }

    @Override
    public String toString() {
        return name;
    }
}
