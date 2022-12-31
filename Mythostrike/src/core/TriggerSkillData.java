package core;

import core.management.GameManager;
import events.EventType;
import events.handle.CardAskHandle;
import events.handle.DamageHandle;
import events.handle.DamageType;
import events.FunctionObserver;

import java.util.List;
import java.util.function.Function;

public enum TriggerSkillData {

    REVENGE("Revenge", "whenever you get damage, judge a card, if it's not heart, "
            + "damage dealer has to drop 2 handcards or get 1 damage dealded by you"
            ,true, List.of(EventType.DAMAGED), new FunctionObserver<>((Function<DamageHandle, Boolean>) damageHandle -> {
                Player victim = damageHandle.getTo();
                Player dealer = damageHandle.getFrom();
                GameManager gameManager = damageHandle.getGameManager();
                Skill skill = victim.hasSkillByName("Revenge");
                if (skill == null) {
                    return false;
                }
                if (gameManager.getGameController().askForSkillInvoke(victim, skill)) {
                    //TODO : use JudgeHandle instead of judge
                    Card judgeCard = gameManager.getCardManager().judge();
                    if (judgeCard.getSymbol() != CardSymbol.HEART) {
                        CardAskHandle cardAskHandle = new CardAskHandle(damageHandle.getGameManager(), null, "drop 2 cards or you get 1 damage deal cause of revenge", dealer, null, 2, gameManager.getGame().getThrowDeck(), true);
                        if (!gameManager.getGameController().askForDiscard(cardAskHandle)) {
                            gameManager.getPlayerManager().applyDamage(new DamageHandle(gameManager, null, "cause of Revenge", victim, dealer, 1, DamageType.NORMAL));

                        }
                    }
                    return true;
                }

                return false;
            }));
    private final String name;
    private final String description;
    private final boolean isActive;
    private final FunctionObserver functionListener;
    private final List<EventType> events;

    TriggerSkillData(String name, String description, boolean isActive,List<EventType> events, FunctionObserver functionListener) {
        this.name = name;
        this.description = description;
        this.isActive = isActive;
        this.functionListener = functionListener;
        this.events = events;
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

    public List<EventType> getEvents() {
        return events;
    }
}
