package com.mythostrike.model.game.player;

import com.mythostrike.controller.GameController;
import com.mythostrike.controller.message.game.ChampionSelectionMessage;
import com.mythostrike.controller.message.game.HighlightMessage;
import com.mythostrike.model.game.Game;
import com.mythostrike.model.game.activity.Card;
import com.mythostrike.model.game.management.GameManager;
import lombok.Getter;

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
        //TODO: what to do?
    }

}
