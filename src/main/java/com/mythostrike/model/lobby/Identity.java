package com.mythostrike.model.lobby;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.util.List;


@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum Identity {
    TEAM_RED("Team Red", false, true),
    TEAM_BLUE("Team Blue", false, true),
    GOD_KING("God King", false, true),
    GENERAL("General", true, false),
    REBEL("Rebel", true, false),
    RENEGADE("Renegade", true, true),
    NOT_SET("not set", false, true);

    @Getter
    private final String name;
    @Getter
    private final boolean incognito;
    private final boolean playerNeedsToBeAlive;
    private List<Identity> hasToSurvive;
    private List<Identity> hasToDie;

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
        REBEL.hasToDie = List.of(GOD_KING, GENERAL, RENEGADE);

        RENEGADE.hasToSurvive = List.of(RENEGADE);
        RENEGADE.hasToDie = List.of(GOD_KING, GENERAL, REBEL);
    }

    Identity(String name, boolean incognito, boolean playerNeedsToBeAlive) {
        this.name = name;
        this.incognito = incognito;
        this.playerNeedsToBeAlive = playerNeedsToBeAlive;
    }


    //TODO: Implement this method
    /*public boolean hasWon(Player player , GameManager gameManager) {

    }*/
}
