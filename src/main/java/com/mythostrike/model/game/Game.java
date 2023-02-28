package com.mythostrike.model.game;

import com.mythostrike.model.game.activity.cards.CardList;
import com.mythostrike.model.game.activity.cards.CardPile;
import com.mythostrike.model.game.activity.cards.CardSpaceType;
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

    //TODO: make a static singelton list of random seeds from all games
    public static final Random RANDOM_SEED = new Random(42);

    public final GameManager gameManager;
    private final List<Player> allPlayers;
    private final List<Player> alivePlayers;
    private final Mode mode;
    @Getter
    private final CardPile allCards;
    @Setter
    private CardPile drawPile;
    @Setter
    private CardPile discardPile;
    @Setter
    private CardPile tablePile;


    public Game(List<Player> players, Mode mode, GameManager gameManager) {
        this.allPlayers = new ArrayList<>(players);
        this.mode = mode;
        this.gameManager = gameManager;
        alivePlayers = new ArrayList<>(allPlayers);

        drawPile = new CardPile(CardSpaceType.DRAW_PILE, CardList.getCardList().getFullCardDeck());
        tablePile = new CardPile(CardSpaceType.TABLE_PILE);
        discardPile = new CardPile(CardSpaceType.DISCARD_PILE);
        allCards = new CardPile(CardSpaceType.ALL_CARDS, drawPile.getCards());
        drawPile.shuffle(RANDOM_SEED);
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

    public List<Player> getOtherPlayers(Player player) {
        List<Player> players = new ArrayList<>(alivePlayers);
        players.remove(player);
        return players;
    }
}
