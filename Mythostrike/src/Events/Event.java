package Events;

import java.util.ArrayList;
import java.util.function.Function;

public enum Event {
    //gives unkown
    GAMESTART,
    //gives Player
    TURNSTART,
    //gives Phase
    PHASESTART,
    //gives Phase
    PHASEPROCEEDING,
    //gives Phase
    PHASEEND,
    //gives PhaseChangeHandle
    PHASECHANGING,
    //gives PhaseChangeHandle
    PHASESKIPPING,

    //gives CardDrawHandle
    DRAWCARDTURN,

    //gives DamageHandle ( with negative damage )
    BEFOREHPRECOVER,
    AFTERHPRECOVER,
    BEFOREHPLOST,
    AFTERHPLOST,

    HPCHANGED,
    MAXHPCHANGED,

    EVENTLOSESKILL,
    EVENTNEEDSKILL,

    BEFOREJUDGE,
    AFTERJUDGE,

    BEFORERACECARD,
    BYRACECARD,
    AFTERRACECARD,

    CHAINSTATECHANGE,


    CONFIRMDAMAGE,
    DAMAGEFORSEEN,
    DAMAGECAUSED,
    DAMAGEINFLICTED,
    BEFOREDAMAGEDONE,
    DAMAGEDONE,
    DAMAGE,
    DAMAGED,
    DAMAGECOMPLETE,

    DYING,
    ASKFORHEAL,
    ASKFORHEALDONE,
    DEATH,

    ATTACKEFFECTED,
    ATTACKPROCEED,
    ATTACKHIT,
    ATTACKMISSED,

    CARDASKED,
    CARDRESPONDED,
    BEFORECARDSMOVE,

    PRECARDUSED,
    CARDUSED,
    TARGETCONFIRMING,
    TARGETCONFIRMED,

    CARDEFFECT,
    CARDEFFECTED,
    AFTERCARDEFFECTED;

    private final ArrayList<Function<?,?>> functionList = new ArrayList<>();

    public void addFunction(Function<?,?> function){functionList.add(function);}

    public void deleteFunction(Function<?,?> function){functionList.remove(function);}

    public ArrayList<Function<?,?>> getFunctionList(){
        return functionList;
    }
    }
