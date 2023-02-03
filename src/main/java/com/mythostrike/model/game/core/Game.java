package com.mythostrike.model.game.core;

import com.mythostrike.model.game.core.activity.cards.CardPile;
import com.mythostrike.model.game.core.activity.cards.CardList;
import com.mythostrike.model.game.core.management.GameManager;
import com.mythostrike.model.game.core.player.Player;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
public class Game {
    /**
     * describe how many cards player start with
     */
    public static final int INITIALCARDCOUNT = 4;
    /**
     * describe how many cards player draw.
     */
    public static final int DRAWCARDCOUNT = 4;
    private final GameManager gameManager;
    private String console;
    private final ArrayList<Player> allPlayers;
    private final ArrayList<Player> alivePlayers;
    private CardList cardList;
    private CardPile drawDeck;
    private CardPile throwDeck;
    private CardPile tableDeck;
    private final Mode mode;


    public Game(ArrayList<Player> players, Mode mode, GameManager gameManager) {
        this.allPlayers = players;
        this.mode = mode;
        this.gameManager = gameManager;
        alivePlayers = new ArrayList<>(allPlayers);
        cardList = CardList.getInstance();
        drawDeck = new CardPile(cardList.getCards());
        tableDeck = new CardPile();
        throwDeck = new CardPile();
    }

    public void nextPlayer() {

        //TODO:implement
    }

    public Player getCurrentPlayer() {
        return alivePlayers.get(0);
    }

    public boolean isPlayerAlive(Player player) {
        return player.isAlive();
    }



    public void output(String output) {
        //TODO: merge with apis
    }

}
