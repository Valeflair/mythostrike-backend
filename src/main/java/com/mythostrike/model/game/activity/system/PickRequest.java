package com.mythostrike.model.game.activity.system;

import com.mythostrike.controller.message.game.HighlightMessage;
import com.mythostrike.model.game.activity.ActiveSkill;
import com.mythostrike.model.game.activity.Activity;
import com.mythostrike.model.game.activity.PassiveSkill;
import com.mythostrike.model.game.activity.cards.Card;
import com.mythostrike.model.game.management.GameManager;
import com.mythostrike.model.game.player.Player;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
public class PickRequest extends Activity {
    public static final String NAME = PickRequest.class.getSimpleName();
    public static final String DESCRIPTION = "you have to pick a card";
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
    @Setter
    private PassiveSkill selectedPassiveSkill;
    @Setter
    private ActiveSkill selectedActiveSkill;

    public PickRequest(Player player, GameManager gameManager, HighlightMessage highlightMessage) {
        super(NAME, DESCRIPTION);
        this.gameManager = gameManager;
        this.player = player;
        this.highlightMessage = highlightMessage;
    }


    @Override
    public void use() {
        gameManager.highlightPickRequest(this);
    }


}
