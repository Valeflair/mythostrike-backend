package com.mythostrike.model.game.core.activity.system;

import com.mythostrike.model.game.core.HighlightMessage;
import com.mythostrike.model.game.core.activity.Activity;
import com.mythostrike.model.game.core.activity.Card;
import com.mythostrike.model.game.core.activity.events.handle.CardUseHandle;
import com.mythostrike.model.game.core.management.GameManager;
import com.mythostrike.model.game.core.player.Player;
import lombok.Getter;
import org.springframework.security.core.parameters.P;

import java.util.ArrayList;
import java.util.List;

@Getter
public class PickCardToPLay extends Activity {
    public static final String NAME = "Draw";
    public static final String DESCRIPTION = "Drawing it's card";
    public static final int ID = -11;
    public static final int CARD_COUNT_TURN_START = 2;

    private GameManager gameManager;
    private List<Card> selectedCards;
    private List<Player> selectedPlayers;

    public PickCardToPLay(GameManager gameManager) {
        super(ID, NAME, DESCRIPTION);
    }

    @Override
    public void use() {
        Player player = gameManager.getGame().getCurrentPlayer();
        List<Card> playableCards = getPlayableCards(player);
        List<Integer> cardIds = new ArrayList<>();
        for (Card card : playableCards) {
            cardIds.add(card.getId());
        }
        HighlightMessage highlightMessage = new HighlightMessage(cardIds, null,
            null, 1, 1, 0, 0, "Pick a Card to play", true);
        PickRequest pickRequest = new PickRequest(player, gameManager, highlightMessage);

        PlayCard playCard = new PlayCard(gameManager);
        gameManager.queueActivity(playCard);
        gameManager.queueActivity(pickRequest);

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
