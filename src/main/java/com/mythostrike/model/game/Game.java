package com.mythostrike.model.game;

import com.mythostrike.model.game.activity.cards.CardList;
import com.mythostrike.model.game.activity.cards.CardPile;
import com.mythostrike.model.game.management.GameManager;
import com.mythostrike.model.game.player.Player;
import com.mythostrike.model.lobby.Mode;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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

    public static final Random RANDOM_SEED = new Random(42);
    private final GameManager gameManager;
    private final List<Player> allPlayers;
    private final List<Player> alivePlayers;
    private final CardList cardList;
    private final Mode mode;
    private String console;
    @Setter
    private CardPile drawPile;
    @Setter
    private CardPile throwPile;
    @Setter
    private CardPile tablePile;


    public Game(List<Player> players, Mode mode, GameManager gameManager) {
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
