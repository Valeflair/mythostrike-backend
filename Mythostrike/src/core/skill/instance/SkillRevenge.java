package core.skill.instance;

import core.card.Card;
import core.card.CardSymbol;
import core.game.Effect;
import core.game.Player;
import core.game.management.EventManager;
import core.game.management.GameManager;
import core.skill.Skill;
import core.skill.events.handle.CardAskHandle;
import core.skill.events.handle.DamageHandle;
import core.skill.events.handle.DamageType;
import core.skill.events.type.EventTypeDamage;

import java.util.function.Function;

public class SkillRevenge extends Skill {
    public static final String name = "Revenge";
    private static final String description = "when you get damage, you can judge a card, if its not red, dealer has to drop a card or get 1 damage by you";

    private static final boolean isActive = false;

    private static final Function<DamageHandle, Boolean> condition = damageHandle -> true;
    private static final Function<DamageHandle, Boolean> function = damageHandle -> {
        Player victim = damageHandle.getTo();
        Player dealer = damageHandle.getFrom();
        GameManager gameManager = damageHandle.getGameManager();
        Skill skill = victim.hasSkillByName("revenge");
        if (skill == null) {
            return false;
        }
        if (gameManager.getGameController().askForSkillInvoke(victim, skill)) {
            //TODO : use JudgeHandle instead of judge
            Card judgeCard = gameManager.getCardManager().judge();
            if (judgeCard.getSymbol() != CardSymbol.HEART) {
                CardAskHandle cardAskHandle = new CardAskHandle(damageHandle.getGameManager(), null, "drop 2 cards or you get 1 damage deal cause of revenge", dealer, dealer.getHandCards() , null, 2, gameManager.getGame().getThrowDeck(), true);
                if (!gameManager.getGameController().askForDiscard(cardAskHandle)) {
                    gameManager.getPlayerManager().applyDamage(new DamageHandle(gameManager, null, "cause of Revenge", victim, dealer, 1, DamageType.NORMAL));

                }
            }
            return true;
        }

        return false;
    };
    private static final Function<DamageHandle, Boolean> activate = damageHandle -> {
        Player victim = damageHandle.getTo();
        Player dealer = damageHandle.getFrom();
        GameManager gameManager = damageHandle.getGameManager();
        Skill skill = victim.hasSkillByName(name);
        if (skill == null) {
            return false;
        }
        return gameManager.getGameController().askForSkillInvoke(victim, skill);
    };

    private static final Effect<DamageHandle> effect = new Effect<>(name, condition, activate,  function);

    private static final Function<EventManager, Boolean> register = eventManager -> {
        eventManager.registerEvent(EventTypeDamage.DAMAGED, effect);
        return true;
    };

    public SkillRevenge() {
        super(name, description, isActive, effect);
    }


    @Override
    public boolean trigger(DamageHandle handle) {
        return function.apply(handle);
    }

    @Override
    public boolean condition(DamageHandle handle) {
        return condition.apply(handle);
    }

    @Override
    public boolean activate(DamageHandle handle) {
        return activate.apply(handle);
    }

    @Override
    public boolean register(EventManager eventManager) {
        return register.apply(eventManager);
    }
}
