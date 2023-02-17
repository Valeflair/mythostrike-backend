package com.mythostrike.model.game.activity.skill.active;

import com.mythostrike.controller.message.game.HighlightMessage;
import com.mythostrike.controller.message.game.PlayerCondition;
import com.mythostrike.model.game.activity.ActiveSkill;
import com.mythostrike.model.game.activity.cards.Card;
import com.mythostrike.model.game.activity.cards.CardFilter;
import com.mythostrike.model.game.activity.cards.CardSymbol;
import com.mythostrike.model.game.activity.cards.cardtype.Attack;
import com.mythostrike.model.game.activity.cards.cardtype.Defend;
import com.mythostrike.model.game.activity.events.handle.CardDrawHandle;
import com.mythostrike.model.game.activity.events.handle.CardMoveHandle;
import com.mythostrike.model.game.activity.events.handle.CardUseHandle;
import com.mythostrike.model.game.activity.events.handle.PlayerHandle;
import com.mythostrike.model.game.activity.system.PickRequest;
import com.mythostrike.model.game.management.GameManager;
import com.mythostrike.model.game.player.Player;

import java.util.ArrayList;
import java.util.List;

public class SynergyOrder extends ActiveSkill {

    public static final String NAME = "Synergy Order";
    public static final String DESCRIPTION = "once per turn, you can drop X cards and draw X cards (X is the number you want)";
    private static final CardFilter DEFEND_FILTER = new CardFilter(Defend.NAME);
    private PickRequest pickRequest;

    public SynergyOrder() {
        super(NAME, DESCRIPTION);
    }

    @Override
    public boolean checkCondition(PlayerHandle playerHandle) {
        boolean value = playerHandle.getPlayer().getActiveSkills().stream()
                .anyMatch(skill -> skill.getName().equals(NAME)
                && playerHandle.getPlayer().isImmune(NAME));


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
        List<Card> cards = player.getHandCards().getCards();
        List<PlayerCondition> playerConditions = new ArrayList<>();

        PlayerCondition playerCondition =
                new PlayerCondition(GameManager.convertPlayersToUsername(targets), List.of(0));
        List<Integer> cardCounts = new ArrayList<>();
        for (int i = 0; i < cards.size(); i++) {
            playerConditions.add(playerCondition);
            cardCounts.add(i);
        }
        cardCounts.add(cards.size());

        HighlightMessage highlightMessage = HighlightMessage.builder()
                .cardIds(GameManager.convertCardsToInteger(cards))
                //0 for not use skill and not play card, 1 for play defend as attack
                .cardCount(cardCounts)
                //for each defend need an individual playerCondition, but they are identical
                .cardPlayerConditions(playerConditions)
                .reason("choose cards to drop and draw same amount of cards")
                .build();
        pickRequest = new PickRequest(player, gameManager, highlightMessage);
        gameManager.queueActivity(this);
        gameManager.queueActivity(pickRequest);

    }

    @Override
    public void use() {
        if (pickRequest.getSelectedCards() == null || pickRequest.getSelectedCards().isEmpty()) {
            return;
        }
        Player player = playerHandle.getPlayer();
        GameManager gameManager = playerHandle.getGameManager();

        CardMoveHandle moveOut = new CardMoveHandle(gameManager,"drop to get synergy order", player, null, player.getHandCards(),
                gameManager.getGame().getTablePile(), pickRequest.getSelectedCards());
        gameManager.getCardManager().moveCard(moveOut);
        CardDrawHandle drawHandle = new CardDrawHandle(gameManager, "draw to get synergy order", player,
                pickRequest.getSelectedCards().size(), gameManager.getGame().getDrawPile());
        gameManager.getCardManager().drawCard(drawHandle);
        player.setTemporaryImmunity(NAME, true);
    }

}
