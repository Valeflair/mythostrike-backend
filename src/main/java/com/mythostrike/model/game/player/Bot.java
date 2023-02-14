package com.mythostrike.model.game.player;

import com.mythostrike.controller.GameController;
import com.mythostrike.controller.message.game.ChampionSelectionMessage;
import com.mythostrike.controller.message.game.HighlightMessage;
import com.mythostrike.model.game.Game;
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
        List<Integer> pickedCards = new ArrayList<>();


        if (message.cardIds() != null && message.cardCount() != null && !message.cardCount().contains(0)) {
            List<Integer> shuffleCount = new ArrayList<>(message.cardCount());
            List<Integer> shuffleIds = new ArrayList<>(message.cardIds());
            Collections.shuffle(shuffleCount, Game.RANDOM_SEED);
            Collections.shuffle(shuffleIds, Game.RANDOM_SEED);

            for (int i = 0; i < shuffleCount.get(0); i++) {
                pickedCards.add(shuffleIds.get(i));
            }
        }
        gameManager.selectCards(getUsername(), pickedCards, new ArrayList<>());
    }
}
