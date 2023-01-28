package com.mythostrike.model.lobby;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;


@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@Getter
public enum Identity {
    TEAM_RED("Team Red", false),
    TEAM_BLUE("Team Blue", false),
    GOD_KING("God King", false),
    GENERAL("General", true),
    REBEL("Rebel", true),
    RENEGADE("Renegade", true),
    NOT_SET("not set", false);

    private final boolean incognito;

    private final String name;
    Identity(String name, boolean incognito) {
        this.name = name;
        this.incognito = incognito;
    }
}
