package core;

import java.util.ArrayList;
import java.util.Arrays;

public enum Mode {


    IDENTITY_FOR_FIVE(new Identity[]{Identity.GODKING, Identity.GENERAL, Identity.REBEL, Identity.REBEL, Identity.RENEGADE}),
    IDENTITY_FOR_EIGHT(new Identity[]{Identity.GODKING, Identity.GENERAL, Identity.GENERAL, Identity.REBEL, Identity.REBEL, Identity.REBEL, Identity.REBEL, Identity.RENEGADE}),
    ONE_VERSUS_ONE(new Identity[]{Identity.TEAMRED,Identity.TEAMBLUE}),
    TWO_VERSUS_TWO(new Identity[]{Identity.TEAMRED,Identity.TEAMRED,Identity.TEAMBLUE,Identity.TEAMBLUE}),
    THREE_VERSUS_THREE(new Identity[]{Identity.TEAMRED,Identity.TEAMRED,Identity.TEAMRED,Identity.TEAMBLUE,Identity.TEAMBLUE,Identity.TEAMBLUE}),
    FOUR_VERSUS_FOUR(new Identity[]{Identity.TEAMRED,Identity.TEAMRED,Identity.TEAMRED,Identity.TEAMRED,Identity.TEAMBLUE,Identity.TEAMBLUE,Identity.TEAMBLUE,Identity.TEAMBLUE});

    private final ArrayList<Identity> identities;

    Mode(Identity[] identities){
        this.identities = new ArrayList<Identity>(Arrays.asList(identities));
    }

    public ArrayList<Identity> getIdentities() {
        return identities;
    }
}
