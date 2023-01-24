package com.mythostrike.model.game.core;

import com.mythostrike.model.game.core.management.GameManager;

import java.util.ArrayList;

public class Game {
    /**
     * describe how many cards player start with
     */
    public static final int INITIALCARDCOUNT = 4;
    /**
     * describe how many cards player draw.
     */
    public static final int DRAWCARDCOUNT = 4;
    private GameManager gameManager;
    private Player currentPlayer;
    private String console;
    private final ArrayList<Player> players;
    private CardDeck drawDeck;
    private CardDeck throwDeck;
    private CardDeck tableDeck;
    private Mode mode;


    public Game(ArrayList<Player> players, Mode mode, GameManager gameManager) {
        this.players = players;
        this.mode = mode;
        this.gameManager = gameManager;
    }

    public void output(String output) {
        System.out.println(output);
        console += output;
        console += "\n";
    }

    public GameManager getGameManager() {
        return gameManager;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public String getConsole() {
        return console;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public CardDeck getDrawDeck() {
        return drawDeck;
    }

    public CardDeck getThrowDeck() {
        return throwDeck;
    }

    public CardDeck getTableDeck() {
        return tableDeck;
    }

    public Mode getMode() {
        return mode;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public void setDrawDeck(CardDeck drawDeck) {
        this.drawDeck = drawDeck;
    }

    public void setThrowDeck(CardDeck throwDeck) {
        this.throwDeck = throwDeck;
    }

    public void setTableDeck(CardDeck tableDeck) {
        this.tableDeck = tableDeck;
    }

    public ArrayList<Player> getOtherPlayers(Player player) {
        ArrayList<Player> otherPlayers = new ArrayList<Player>(players);
        otherPlayers.remove(player);
        return otherPlayers;
    }
}
