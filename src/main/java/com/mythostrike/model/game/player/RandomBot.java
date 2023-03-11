package com.mythostrike.model.game.player;

import com.mythostrike.controller.message.game.ChampionSelectionMessage;
import com.mythostrike.controller.message.game.HighlightMessage;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;

@Slf4j
/**
 * This Bot selects random cards or skills. It tries to not select 0 cards or skills if there are other possibilities
 * @Author Till, Jack
 * @Version 1.0
 */
public class RandomBot extends Bot {
    public RandomBot(String name) {
        super(name);
    }

    @Override
    public void selectChampionFrom(ChampionSelectionMessage message) {
        try {
            sleep(DELAY_BEFORE_ACTION);
        } catch (InterruptedException e) {
            //ignore
            log.warn("Interrupted while waiting for action delay");
        }
        gameManager.selectChampion(getUsername(), Bot.selectRandomValue(message.champions(), false));
    }

    @Override
    protected boolean wantTurnEnd(HighlightMessage message) {
        return false;
    }

    @Override
    protected List<Integer> pickRandomCards(List<Integer> cardIds, List<Integer> cardCount) {
        List<Integer> tempCardCount = new ArrayList<>(cardCount);

        //select an amount possible for the cardIds
        int amount = 0;
        boolean selectOtherThenZero = false;
        boolean selectedZero = false;
        do {

            amount = Bot.selectRandomValue(tempCardCount, true);
            selectOtherThenZero = tempCardCount.size() > 1 && amount == 0;

            //if we selected 0, maybe we need to select it because there is no other option
            if (selectOtherThenZero) {
                selectedZero = true;
            }
            if (tempCardCount.isEmpty() && selectedZero) {
                return new ArrayList<>();
            }
        } while (amount > cardIds.size() || selectOtherThenZero);

        //select amount random cards from cardIds
        return new ArrayList<>(Bot.selectRandomValues(cardIds, amount));
    }

    @Override
    protected int pickRandomSkill(List<Integer> skillIds, List<Integer> skillCount) {
        List<Integer> tempCardCount = new ArrayList<>(skillCount);

        //select an amount possible for the cardIds
        int amount = 0;
        boolean selectOtherThenZero = false;
        boolean selectedZero = false;
        do {
            amount = Bot.selectRandomValue(tempCardCount, true);
            selectOtherThenZero = tempCardCount.size() > 1 && amount == 0;

            //if we selected 0, maybe we need to select it because there is no other option
            if (selectOtherThenZero) {
                selectedZero = true;
            }
            if (tempCardCount.isEmpty() && selectedZero) {
                return NO_SKILL_PICKED;
            }
        } while (amount > skillIds.size() || selectOtherThenZero);

        if (amount < 0 || amount > 1) {
            throw new IllegalArgumentException("you should only be able to select one skill");
        }

        if (amount == 0) {
            return NO_SKILL_PICKED;
        } else {
            return Bot.selectRandomValue(skillIds, false);
        }
    }

    @Override
    protected List<String> pickRandomPlayers(List<String> players, List<Integer> playerCount) {
        List<Integer> tempCardCount = new ArrayList<>(playerCount);

        //select an amount possible for the cardIds
        int amount = 0;
        boolean selectOtherThenZero;
        boolean selectedZero = false;
        do {
            amount = Bot.selectRandomValue(tempCardCount, true);
            selectOtherThenZero = tempCardCount.size() > 1 && amount == 0;

            //if we selected 0, maybe we need to select it because there is no other option
            if (selectOtherThenZero) {
                selectedZero = true;
            }
            if (tempCardCount.isEmpty() && selectedZero) {
                return new ArrayList<>();
            }
        } while (amount > players.size() || selectOtherThenZero);

        //select amount random cards from cardIds
        return new ArrayList<>(Bot.selectRandomValues(players, amount));
    }
}
