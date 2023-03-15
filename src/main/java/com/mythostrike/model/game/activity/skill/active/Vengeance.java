package com.mythostrike.model.game.activity.skill.active;

import com.mythostrike.controller.message.game.HighlightMessage;
import com.mythostrike.controller.message.game.PlayerCondition;
import com.mythostrike.model.game.activity.ActiveSkill;
import com.mythostrike.model.game.activity.cards.Card;
import com.mythostrike.model.game.activity.cards.CardFilter;
import com.mythostrike.model.game.activity.cards.CardType;
import com.mythostrike.model.game.activity.events.handle.CardDrawHandle;
import com.mythostrike.model.game.activity.events.handle.CardMoveHandle;
import com.mythostrike.model.game.activity.events.handle.PlayerHandle;
import com.mythostrike.model.game.activity.system.PickRequest;
import com.mythostrike.model.game.management.GameManager;
import com.mythostrike.model.game.player.Player;

import java.util.ArrayList;
import java.util.List;

public class Vengeance extends ActiveSkill {

    public static final String NAME = "Vengeance";
    public static final String DESCRIPTION =
        "Once per turn you can drop a Weapon or sacrifice 1 HP to attack a player";
    private PickRequest pickRequest;

    public Vengeance() {
        super(NAME, DESCRIPTION);
        CardFilter WEAPON_FILTER = new CardFilter();
        WEAPON_FILTER.addIncludeType(CardType.WEAPON);
    }

    @Override
    public boolean checkCondition(PlayerHandle playerHandle) {
        boolean value = playerHandle.getPlayer().getActiveSkills().stream()
            .anyMatch(skill -> skill.getName().equals(NAME)
                && !playerHandle.getPlayer().isImmune(NAME)
                && playerHandle.getPlayer().getHandCards().size() != 0);


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

        PlayerCondition playerCondition
            = new PlayerCondition(GameManager.convertPlayersToUsername(targets), List.of(0));
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

        CardMoveHandle moveOut
            = new CardMoveHandle(gameManager, "drop to get vengace", player, null, player.getHandCards(),
                gameManager.getGame().getTablePile(), pickRequest.getSelectedCards());
        gameManager.getCardManager().moveCard(moveOut);
        CardDrawHandle drawHandle = new CardDrawHandle(gameManager, "draw to get vengance", player,
            pickRequest.getSelectedCards().size(), gameManager.getGame().getDrawPile());
        gameManager.getCardManager().drawCard(drawHandle);
        player.setTemporaryImmunity(NAME, true);
    }

}
