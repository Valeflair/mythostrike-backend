package com.mythostrike.model.game.core.activity.system;

import com.mythostrike.model.game.core.HighlightMessage;
import com.mythostrike.model.game.core.activity.Activity;
import com.mythostrike.model.game.core.activity.Card;
import com.mythostrike.model.game.core.activity.events.handle.CardUseHandle;
import com.mythostrike.model.game.core.management.GameManager;
import com.mythostrike.model.game.core.player.Player;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class PickRequest extends Activity {
    public static final String NAME = "Draw";
    public static final String DESCRIPTION = "Drawing it's card";
    public static final int ID = -11;
    public static final int CARD_COUNT_TURN_START = 2;

    private GameManager gameManager;
    private Player player;
    @Setter
    private List<Card> selectedCards;
    @Setter
    private List<Player> selectedPlayers;
    private HighlightMessage highlightMessage;

    public PickRequest(Player player, GameManager gameManager, HighlightMessage highlightMessage) {
        super(ID, NAME, DESCRIPTION);
        this.gameManager = gameManager;
        this.player = player;
        this.highlightMessage = highlightMessage;
    }

    @Override
    public void use() {
        Player player = gameManager.getGame().getCurrentPlayer();
        List<Card> playableCards = getPlayableCards(player);

        PlayCard playCard = new PlayCard(gameManager);
        gameManager.queueActivity(playCard);
        //TODO implement with highlightmessage
        //highlightmessage should contain a class PlayCard added into gamemanager and use setTarget or setCard before call back


    }

    private List<Card> getPlayableCards(Player player) {

        List<Card> playableCards = new ArrayList<>();
        for (Card card : player.getHandCards().getCards()) {
            CardUseHandle cardUseHandle = new CardUseHandle(
                gameManager, card, "check if card is playable", player, player,
                true);
            if (card.checkCondition(cardUseHandle)) {
                playableCards.add(card);
            }
        }
        return playableCards;
    }

}
