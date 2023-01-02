package skill;

import core.Card;
import core.CardSymbol;
import core.Player;
import core.management.GameManager;
import skill.events.EventType;
import skill.events.FunctionObserver;
import skill.events.handle.CardAskHandle;
import skill.events.handle.DamageHandle;
import skill.events.handle.DamageType;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class TriggerSkillInitialize {

    public ArrayList<TriggerSkill<?>> initialize() {
        ArrayList<TriggerSkill<?>> skills = new ArrayList<>();
        Function<DamageHandle, Boolean> function = damageHandle -> {
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
                    CardAskHandle cardAskHandle = new CardAskHandle(damageHandle.getGameManager(), null, "drop 2 cards or you get 1 damage deal cause of revenge", dealer, dealer.getHandCards() , null, 2, gameManager.getGame().getThrowDeck(), true);
                    if (!gameManager.getGameController().askForDiscard(cardAskHandle)) {
                        gameManager.getPlayerManager().applyDamage(new DamageHandle(gameManager, null, "cause of Revenge", victim, dealer, 1, DamageType.NORMAL));

                    }
                }
                return true;
            }

            return false;
        };
        TriggerSkill<?> skill = new TriggerSkill<>("revenge", "revenger", true, new FunctionObserver<DamageHandle>(function), List.of(EventType.DAMAGED));
        skills.add(skill);
        return skills;
    }
}
