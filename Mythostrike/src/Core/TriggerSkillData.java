package Core;

import Events.Handle.DamageHandle;
import Events.Handle.DamageType;
import Events.EventType;
import Events.Observers.FunctionObserver;

import java.util.function.Function;

public enum TriggerSkillData {

    REVENGE("Revenge", "whenever you get damage, judge a card, if it's not heart, " +
            "damage dealer has to drop 2 handcards or get 1 damage dealded by you"
            ,true, new FunctionObserver<>(EventType.DAMAGED, (Function<DamageHandle, Boolean>) damageHandle -> {
                Player victim = damageHandle.getTo();
                Player dealer = damageHandle.getFrom();
        GameController gameController = damageHandle.getGameController();
                Skill skill = victim.hasSkillByName("Revenge");
                if (skill == null) {
                    return false;
                }
                if (gameController.askForSkillInvoke(victim, skill)) {
                    //TODO : use JudgeHandle instead of judge
                    Card judgeCard = gameController.judge();
                    if (judgeCard.getSymbol() != CardSymbol.HEART) {
                        if (!gameController.askForDiscard(dealer, dealer.getHandCards(), 2, 2, true, "Drop 2 HandCard cause of Revenge, or you can dont drop and get 1 Damage")) {
                            gameController.applyDamage(new DamageHandle(gameController, null, "cause of Revenge", victim, dealer, 1, DamageType.NORMAL));

                        }
                    }
                    return true;
                }

                return false;
            }))
    ;
    private final String name;
    private final String description;
    private final boolean isActive;
    private final FunctionObserver functionListener;

    TriggerSkillData(String name, String description, boolean isActive, FunctionObserver functionListener) {
        this.name = name;
        this.description = description;
        this.isActive = isActive;
        this.functionListener = functionListener;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public boolean isActive() {
        return isActive;
    }

    public FunctionObserver getFunctionListener() {
        return functionListener;
    }
}
