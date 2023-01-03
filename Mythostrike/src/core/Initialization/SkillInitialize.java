package core.Initialization;

import core.Card;
import core.CardSymbol;
import core.Effect;
import core.Player;
import core.management.EventManager;
import core.management.GameManager;
import skill.Skill;
import skill.TriggerSkill;
import skill.events.handle.CardAskHandle;
import skill.events.handle.DamageHandle;
import skill.events.handle.DamageType;
import skill.events.type.EventTypeDamage;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class SkillInitialize {

    public static List<Skill> initialize(EventManager eventManager) {
        ArrayList<Skill> skills = new ArrayList<>();
        //initialize skill revenge
        TriggerSkill<DamageHandle, EventTypeDamage> skill = initRevenge(eventManager);
        skills.add(skill);
        //intiailize skill ...


        return skills;
    }

    private static TriggerSkill<DamageHandle, EventTypeDamage> initRevenge(EventManager eventManager) {
        String name = "revenge";
        String description = "when you get damage, you can judge a card, if its not red, dealer has to drop a "
                + "card or get 1 damage by you";
        boolean isActive = true;
        Function<DamageHandle, Boolean> condition = damageHandle ->
                damageHandle.getTo().hasSkillByName("revenge") != null;
        Function<DamageHandle, Boolean> function = damageHandle -> {
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
        Effect<DamageHandle> effect = new Effect<>(condition, function);
        Function<EventManager, Boolean> register = eventManager1 -> {
            eventManager.registerEvent(EventTypeDamage.DAMAGED, effect);
            return true;
        };

        TriggerSkill<DamageHandle, EventTypeDamage> skill = new TriggerSkill<>(name, description, isActive,
                register, effect, EventTypeDamage.DAMAGED);
        eventManager.registerEvent(EventTypeDamage.DAMAGED, effect);
        return skill;
    }

    public static Skill getSkillByName(List<Skill> skills, String name) {
        for (Skill skill : skills) {
            if (skill.equals(name)) {
                return skill;
            }
        }
        return null;
    }
}
