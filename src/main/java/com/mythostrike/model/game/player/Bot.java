package com.mythostrike.model.game.player;

import com.mythostrike.controller.GameController;
import com.mythostrike.controller.message.game.ChampionSelectionMessage;
import com.mythostrike.controller.message.game.HighlightMessage;
import com.mythostrike.model.game.Game;
import com.mythostrike.model.game.activity.ActiveSkill;
import com.mythostrike.model.game.activity.Card;
import com.mythostrike.model.game.management.GameManager;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
public class Bot extends Player {

    //TODO: add diffrent difficulty (placeholder(current), random, normal, cheater)
    private int difficulty;
    private GameManager gameManager;

    private GameController gameController;


    public Bot(String name, int difficulty) {
        super(name);
    }

    @Override
    public void initialize(GameManager gameManager) {
        this.gameManager = gameManager;
        this.gameController = gameManager.getGameController();
    }


    public void selectChampionFrom(ChampionSelectionMessage message) {
        gameManager.selectChampion(getUsername(), message.champions().get(0));
    }

    public void highlight(HighlightMessage message) {

        if (message.activateEndTurn()) {
            gameManager.endTurn(getUsername());
            return;
        }
        //it does random shit
        List<Player> players;
        List<Card> cards;
        List<ActiveSkill> skills;

        if (message.activateCancel()) {
            gameManager.cancelRequest(getUsername());
            return;
        }


        if (message.players() != null && message.minPlayer() > 0) {
            int min = message.minPlayer();
            int max = message.maxPlayer();
            List<Player> playerPickList = gameManager.convertUserNameToPlayers(message.players());
            if (playerPickList.isEmpty() || max == 0) {
                players = new ArrayList<>();
            } else {
                int n = Math.min(playerPickList.size(), max);
                n = Math.max(min, n);
                n = Game.RANDOM_SEED.nextInt(n + 1);
                List<Player> result = new ArrayList<>(playerPickList);
                Collections.shuffle(result, Game.RANDOM_SEED);
                players = result.subList(0, n);
            }
            gameManager.selectPlayers(getUsername(), GameManager.convertPlayersToUsername(players));
            if (message.activateConfirm()) {
                gameManager.cancelRequest(getUsername());
            }
            return;
        }
        if (message.cardsId() != null && message.minCard() > 0) {
            int min = message.minCard();
            int max = message.maxCard();
            List<Card> cardPickList = gameManager.convertIdToCards(message.cardsId());
            if (cardPickList.isEmpty() || max == 0) {
                cards = new ArrayList<>();
            } else {
                int n = Math.min(cardPickList.size(), max);
                n = Math.max(min, n);
                n = Game.RANDOM_SEED.nextInt(n + 1);
                List<Card> result = new ArrayList<>(cardPickList);
                Collections.shuffle(result, Game.RANDOM_SEED);
                cards = result.subList(0, n);
            }
            gameManager.selectCards(getUsername(), GameManager.convertCardsToInteger(cards));
            if (message.activateConfirm()) {
                gameManager.cancelRequest(getUsername());
            }
            return;
        }
    }
}
