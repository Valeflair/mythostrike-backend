package com.mythostrike.model.game.player;

import com.mythostrike.model.game.activity.Card;
import com.mythostrike.model.game.Game;
import com.mythostrike.controller.GameController;
import lombok.Getter;

import java.util.List;

@Getter
public class Bot extends Player {
    private int difficulty;
    private Game game;
    private GameController gameController;


    public Bot(String name, int difficulty) {
        super(name);
    }

    public void makeMove() {
        //TODO:implement
    }

    public void selectTargets(List<Player> players, int min, int max) {
        //TODO:implement
    }

    public void selectCards(List<Card> cards, int min, int max) {
        //TODO:implement
    }
}
