package com.mythostrike.model.game.activity.skill.active;


import com.mythostrike.controller.message.game.HighlightMessage;
import com.mythostrike.controller.message.game.PlayerCondition;
import com.mythostrike.model.game.activity.ActiveSkill;
import com.mythostrike.model.game.activity.Card;
import com.mythostrike.model.game.activity.cards.CardFilter;
import com.mythostrike.model.game.activity.cards.CardSymbol;
import com.mythostrike.model.game.activity.cards.cardtype.Attack;
import com.mythostrike.model.game.activity.cards.cardtype.Defend;
import com.mythostrike.model.game.activity.events.handle.CardUseHandle;
import com.mythostrike.model.game.activity.events.handle.PlayerHandle;
import com.mythostrike.model.game.activity.system.PickRequest;
import com.mythostrike.model.game.management.GameManager;
import com.mythostrike.model.game.player.Player;

import java.util.ArrayList;
import java.util.List;

public class Warrior extends ActiveSkill {

    public static final String NAME = Warrior.class.getSimpleName();
    public static final String DESCRIPTION = "you can play defense as attack if you want";
    private static final CardFilter DEFEND_FILTER = new CardFilter(Defend.NAME);
    private final Attack attack;
    private PickRequest pickRequest;

    public Warrior() {
        super(NAME, DESCRIPTION);
        attack = new Attack(-1, CardSymbol.NO_SYMBOL, -1);
    }

    @Override
    public boolean checkCondition(PlayerHandle playerHandle) {
        boolean value = attack.checkCondition(new CardUseHandle(playerHandle.getGameManager(), attack,
            "check if card is playable", playerHandle.getPlayer(), playerHandle.getPlayer(), false))
            && playerHandle.getPlayer().getHandCards().getCards().stream().anyMatch(DEFEND_FILTER::match);

        if (value) {
            this.playerHandle = playerHandle;
            this.playerCondition = new PlayerCondition();
        }
        return value;
    }

    @Override
    public void activate() {
        Player player = playerHandle.getPlayer();
        GameManager gameManager = playerHandle.getGameManager();

        List<Player> targets = new ArrayList<>();
        List<Card> cards = DEFEND_FILTER.filter(player.getHandCards().getCards());
        List<PlayerCondition> playerConditions = new ArrayList<>();

        for (Player target : playerHandle.getGameManager().getGame().getOtherPlayers(player)) {
            if (!target.equals(player) && target.isAlive() && Boolean.FALSE.equals(target.isImmune(NAME))) {
                targets.add(target);
            }
        }

        PlayerCondition playerCondition =
            new PlayerCondition(GameManager.convertPlayersToUsername(targets), List.of(1));

        for (Card card : cards) {
            playerConditions.add(playerCondition);
        }


        HighlightMessage highlightMessage = HighlightMessage.builder()
            .cardIds(GameManager.convertCardsToInteger(cards))
            //0 for not use skill and not play card, 1 for play defend as attack
            .cardCount(List.of(0, 1))
            //for each defend need an individual playerCondition, but they are identical
            .cardPlayerConditions(playerConditions)
            .reason("choose defend to play as attack, and then choose player to attack")
            .build();
        pickRequest = new PickRequest(player, gameManager, highlightMessage);

        gameManager.queueActivity(pickRequest);
    }

    @Override
    public void use() {
        if (pickRequest.getSelectedCards() == null || pickRequest.getSelectedCards().isEmpty()) {
            return;
        }
        attack.getCardUseHandle().setOpponents(pickRequest.getSelectedPlayers());
        attack.setPickRequest(pickRequest);
        playerHandle.getGameManager().queueActivity(attack);
        attack.attacksPlayer(pickRequest.getSelectedPlayers().get(0));
    }

}
