package Events;

public enum EventType {
    //Function<Game,boolean>
    GAME_START,
    //Function<Player,boolean>
    TURN_START,
    //Function<Phase,boolean>
    PHASE_START,
    PHASE_PROCEEDING,
    PHASE_END,
    PHASE_CHANGING,
    PHASE_SKIPPING,

    DRAW_CARD,

    BEFORE_HP_RECOVER,
    AFTER_HP_RECOVER,
    BEFORE_HP_LOST,
    AFTER_HP_LOST,

    HP_CHANGED,
    MAX_HP_CHANGED,

    EVENT_LOSE_SKILL,
    EVENT_NEED_SKILL,

    BEFORE_JUDGE,
    AFTER_JUDGE,

    BEFORE_RACE_CARD,
    BY_RACE_CARD,
    AFTER_RACE_CARD,

    CHAINSTATECHANGE,


    CONFIRM_DAMAGE,
    DAMAGE_FOR_SEEN,
    DAMAGE_CAUSED,
    DAMAGE_INFLICTED,
    BEFORE_DAMAGE_DONE,
    DAMAGE_DONE,
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

    CARD_ASKED,
    CARD_RESPONDED,
    BEFORE_CARDSMOVE,

    PRE_CARD_USED,
    CARD_USED,
    TARGET_CONFIRMING,
    TARGET_CONFIRMED,

    CARD_EFFECT,
    CARD_EFFECTED,
    AFTER_CARD_EFFECTED;


    }
