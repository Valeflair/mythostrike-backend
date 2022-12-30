package Core;

import java.util.ArrayList;
import java.util.List;

public class Game {
    /**
     * describe how many cards player start with
     */
    public static final int INITIALCARDCOUNT = 4;
    /**
     * describe how many cards player draw.
     */
    public static final int DRAWCARDCOUNT = 4;
    GameController gameController;
    Player currentPlayer;
    String console;
    ArrayList<Player> players;
    CardDeck drawDeck;
    CardDeck throwDeck;
    CardDeck tableDeck;
    Mode mode;


    public Game(ArrayList<Player> players,Mode mode){
        this.players = players;
        this.mode = mode;
        gameController = new GameController(this);
    }

    public void output(String output){
        System.out.println(output);
        console += output;
        console += "\n";
    }

    public GameController getGameController() {
        return gameController;
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
