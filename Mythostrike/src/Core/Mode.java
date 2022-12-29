package Core;

import java.util.ArrayList;
import java.util.Arrays;

public enum Mode {


    IDENTITYFORFIVE(new Identity[]{Identity.GODKING, Identity.GENERAL, Identity.REBEL, Identity.REBEL, Identity.RENEGADE}),
    IDENTITYFOREIGHT(new Identity[]{Identity.GODKING, Identity.GENERAL, Identity.GENERAL, Identity.REBEL, Identity.REBEL, Identity.REBEL, Identity.REBEL, Identity.RENEGADE}),
    ONEVERSUSONE(new Identity[]{Identity.TEAMRED,Identity.TEAMBLUE}),
    TWOVERSUSTWO(new Identity[]{Identity.TEAMRED,Identity.TEAMRED,Identity.TEAMBLUE,Identity.TEAMBLUE}),
    THREEVERSUSTHREE(new Identity[]{Identity.TEAMRED,Identity.TEAMRED,Identity.TEAMRED,Identity.TEAMBLUE,Identity.TEAMBLUE,Identity.TEAMBLUE}),
    FOURVERSUSFOUR(new Identity[]{Identity.TEAMRED,Identity.TEAMRED,Identity.TEAMRED,Identity.TEAMRED,Identity.TEAMBLUE,Identity.TEAMBLUE,Identity.TEAMBLUE,Identity.TEAMBLUE});

    final ArrayList<Identity> identities;

    Mode(Identity[] identities){
        this.identities = new ArrayList<Identity>(Arrays.asList(identities));
    }
}
