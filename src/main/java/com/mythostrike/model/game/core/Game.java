package com.mythostrike.model.game.core;

import com.mythostrike.model.game.core.activity.cards.CardPile;
import com.mythostrike.model.game.core.activity.cards.CardList;
import com.mythostrike.model.game.core.management.GameManager;
import com.mythostrike.model.game.core.player.Player;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

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
    private final CardList cardList;
    @Setter
    private CardPile drawPile;
    @Setter
    private CardPile throwPile;
    @Setter
    private CardPile tablePile;
    private final Mode mode;


    public Game(ArrayList<Player> players, Mode mode, GameManager gameManager) {
        this.allPlayers = players;
        this.mode = mode;
        this.gameManager = gameManager;
        alivePlayers = new ArrayList<>(allPlayers);
        cardList = new CardList(CardList.getInstance().getCards());
        drawPile = new CardPile(cardList.getCards());
        tablePile = new CardPile();
        throwPile = new CardPile();
    }

    public void nextPlayer() {
        alivePlayers.add(alivePlayers.remove(0));
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

    public List<Player> getOtherPlayers(Player player) {
        List<Player> players = new ArrayList<>(alivePlayers);
        alivePlayers.remove(player);
        return players;
    }

    public boolean isGameOver() {
        //TODO implement
        return false;
    }
}
