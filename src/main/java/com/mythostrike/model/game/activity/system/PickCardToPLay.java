package com.mythostrike.model.game.activity.system;

import com.mythostrike.controller.message.game.HighlightMessage;
import com.mythostrike.model.game.activity.Activity;
import com.mythostrike.model.game.activity.Card;
import com.mythostrike.model.game.activity.events.handle.CardUseHandle;
import com.mythostrike.model.game.management.GameManager;
import com.mythostrike.model.game.player.Player;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class PickCardToPLay extends Activity {
    public static final String NAME = "Pick Card to Play";
    public static final String DESCRIPTION = "Drawing it's card";
    public static final int ID = -11;
    public static final int CARD_COUNT_TURN_START = 2;

    private final GameManager gameManager;
    private List<Card> selectedCards;
    private List<Player> selectedPlayers;

    public PickCardToPLay(GameManager gameManager) {
        super(ID, NAME, DESCRIPTION);
        this.gameManager = gameManager;
    }

    @Override
    public void use() {
        Player player = gameManager.getGame().getCurrentPlayer();
        List<Card> playableCards = getPlayableCards(player);
        List<Integer> cardIds = GameManager.convertCardsToInteger(playableCards);
        HighlightMessage highlightMessage = new HighlightMessage(cardIds, null,
            null, 1, 1, 0, 0, "Pick a Card to play", true, false);
        PickRequest pickRequest = new PickRequest(player, gameManager, highlightMessage);

        PlayCard playCard = new PlayCard(gameManager, pickRequest);
        gameManager.queueActivity(playCard);
        gameManager.queueActivity(pickRequest);


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
