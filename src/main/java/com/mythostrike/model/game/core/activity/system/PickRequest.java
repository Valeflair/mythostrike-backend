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
    public static final String NAME = "pickreqeust";
    public static final String DESCRIPTION = "you have to pick card";
    public static final int ID = -11;
    public static final int CARD_COUNT_TURN_START = 2;

    private final GameManager gameManager;
    private final Player player;
    @Setter
    private List<Card> selectedCards;
    @Setter
    private List<Player> selectedPlayers;
    @Setter
    private boolean clickedCancel;
    @Setter
    private HighlightMessage highlightMessage;

    public PickRequest(Player player, GameManager gameManager, HighlightMessage highlightMessage) {
        super(ID, NAME, DESCRIPTION);
        this.gameManager = gameManager;
        this.player = player;
        this.highlightMessage = highlightMessage;
    }


    @Override
    public void use() {
        //highlight message to frontend and get feedback in data base
        /*
         * use() -> gameController.highlight(this)
         * ....
         * in gameController : {
         *     pickRequest.setSelectedCards(...);
         *     pickRequest.setSelectedPlayers(...);
         *     pickRequest.setClickedCancel(...);
         *     gameManager.proceed();
         * }
         */
        //TODO implement with highlightmessage
        //TODO think how to keep card like duel consist in acitivity list
        //TODO dont forget the VIEW_AS skill,think how to do
    }



}
