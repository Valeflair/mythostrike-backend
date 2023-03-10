package com.mythostrike.model.game.player;

import com.mythostrike.controller.message.game.ChampionSelectionMessage;
import com.mythostrike.controller.message.game.HighlightMessage;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;

@Slf4j
/**
 * This Bot tries to end its turn immediatly. If not possible it selects random cards.
 * @Author Till, Jack
 * @Version 1.0
 */
public class PlaceholderBot extends Bot {
    public PlaceholderBot(String name) {
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
        gameManager.selectChampion(getUsername(), message.champions().get(0));
    }

    @Override
    protected boolean wantTurnEnd(HighlightMessage message) {
        return message.activateEndTurn();
    }

    @Override
    protected List<Integer> pickRandomCards(List<Integer> cardIds, List<Integer> cardCount) {
        //tries to select zero cards if possible
        if (cardCount.contains(0)) {
            return new ArrayList<>();
        }

        List<Integer> tempCardCount = new ArrayList<>(cardCount);

        //select an amount possible for the cardIds
        int amount = 0;
        do {
            amount = Bot.selectRandomValue(tempCardCount, true);
        } while(amount > cardIds.size());

        //select amount random cards from cardIds
        return new ArrayList<>(Bot.selectRandomValues(cardIds, amount));
    }

    @Override
    protected int pickRandomSkill(List<Integer> skillIds, List<Integer> skillCount) {
        //tries to select zero cards if possible
        if (skillCount.contains(0)) {
            return NO_SKILL_PICKED;
        }

        List<Integer> tempCardCount = new ArrayList<>(skillCount);

        //select an amount possible for the cardIds
        int amount = 0;
        do {
            amount = Bot.selectRandomValue(tempCardCount, true);
        } while(amount >  skillIds.size());

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
        //tries to select zero cards if possible
        if (playerCount.contains(0)) {
            return new ArrayList<>();
        }

        List<Integer> tempCardCount = new ArrayList<>(playerCount);

        //select an amount possible for the cardIds
        int amount = 0;
        do {
            amount = Bot.selectRandomValue(tempCardCount, true);
        } while(amount > players.size());

        //select amount random cards from cardIds
        return new ArrayList<>(Bot.selectRandomValues(players, amount));
    }
}
