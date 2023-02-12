package com.mythostrike.model.lobby;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

import static com.mythostrike.model.lobby.ModeData.Constants.IDENTITY_DESCRIPTION;
import static com.mythostrike.model.lobby.ModeData.Constants.TEAM_FIGHT_DESCRIPTION;

@Getter
@AllArgsConstructor
public enum ModeData {
    FREE_FOR_ALL("Free for all", 2, 8,
        "Free for all is a mode where everyone is against everyone. "
            + "There is no team, no alliance, no mercy. "
            + "Only the strongest will survive.",
        List.of(Identity.NONE, Identity.NONE, Identity.NONE, Identity.NONE, Identity.NONE,
            Identity.NONE, Identity.NONE, Identity.NONE)),

    ONE_VS_ONE("1 vs. 1", 2, 2,
        "1 vs 1 is a duel of fates. No interference, just a show of simple skills and luck."
            + " May the better one win.",
        List.of(Identity.TEAM_RED, Identity.TEAM_BLUE)),

    TWO_VS_TWO("2 vs. 2", 4, 4, "Team fight with 2 players per team."
        + TEAM_FIGHT_DESCRIPTION,
        List.of(Identity.TEAM_RED, Identity.TEAM_RED, Identity.TEAM_BLUE, Identity.TEAM_BLUE)),

    THREE_VS_THREE("3 vs. 3", 6, 6, "Team fight with 3 players per team."
        + TEAM_FIGHT_DESCRIPTION,
        List.of(Identity.TEAM_RED, Identity.TEAM_RED, Identity.TEAM_RED, Identity.TEAM_BLUE,
            Identity.TEAM_BLUE, Identity.TEAM_BLUE)),

    FOUR_VS_FOUR("4 vs. 4", 8, 8, "Team fight with 4 players per team."
        + TEAM_FIGHT_DESCRIPTION,
        List.of(Identity.TEAM_RED, Identity.TEAM_RED, Identity.TEAM_RED, Identity.TEAM_RED,
            Identity.TEAM_BLUE, Identity.TEAM_BLUE, Identity.TEAM_BLUE, Identity.TEAM_BLUE)),

    IDENTITY_FOR_FIVE("Identity for 5 Players", 5, 5, IDENTITY_DESCRIPTION
        + "In Identity for 5 players there is a God King, a General, 2 Rebels and a Renegade.",
        List.of(Identity.GOD_KING, Identity.GENERAL, Identity.REBEL, Identity.REBEL,
            Identity.RENEGADE)),

    IDENTITY_FOR_EIGHT("Identity for 8 Players", 8, 8, IDENTITY_DESCRIPTION
        + "In Identity for 8 players there is a God King, 2 Generals, 4 Rebels and a Renegade.",
        List.of(Identity.GOD_KING, Identity.GENERAL, Identity.GENERAL,
            Identity.REBEL, Identity.REBEL, Identity.REBEL, Identity.REBEL, Identity.RENEGADE));

    private final String name;
    private final int minPlayer;
    private final int maxPlayer;
    private final String description;
    private final List<Identity> identityList;


    //need for Constants in Enums because otherwise we would have an illegal forward reference
    static class Constants {
        public static final String IDENTITY_DESCRIPTION = "Identity is a mode where you "
            + "have a specific role to play. You have to find out who is who and who is on your side."
            + "But pay Attention because it could be that your friend is your enemy and your enemy is your friend."
            + "You can be a God King, a General, a Rebel or a Renegade."
            + "The God King is the most powerful player and is the only one with an public identity."
            + "All other identities are secret and shouldn't be disclosed. "
            + "The Generals have to protect the God King from the Rebels who want to kill him. "
            + "And maybe also the Renegade in the Dark is coming for the God King, because he wants to be a solo star "
            + "by surviving alone.";

        public static final String TEAM_FIGHT_DESCRIPTION = "Try to survive as a team with tactics, "
            + "friendship and power. The last team standing wins.";
    }
}
