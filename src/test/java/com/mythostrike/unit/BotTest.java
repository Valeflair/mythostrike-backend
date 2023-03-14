package com.mythostrike.model.game.player;


import com.mythostrike.controller.GameController;
import com.mythostrike.controller.message.game.HighlightMessage;
import com.mythostrike.controller.message.game.PlayerCondition;
import com.mythostrike.controller.message.lobby.ChampionMessage;
import com.mythostrike.controller.message.lobby.ChampionSelectionMessage;
import com.mythostrike.model.game.activity.system.PickRequest;
import com.mythostrike.model.game.management.GameManager;
import com.mythostrike.model.lobby.Identity;
import com.mythostrike.model.lobby.Mode;
import com.mythostrike.model.lobby.ModeData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BotTest {

    private GameManager gameManager;
    @Mock
    private GameController gameController;
    private Bot bot;
    private Bot bot2;

    private final String TEST_BOT_NAME = "PlaceHolderBot";
    private final Random RANDOM_SEED = new Random(50);

    @BeforeEach
    public void setUp() {

        gameController = new GameController(null);
        bot = new PlaceholderBot(TEST_BOT_NAME);
        bot2 = new RandomBot("Random Bot");

        gameManager = new GameManager(List.of(bot), new Mode(100, ModeData.FREE_FOR_ALL)
            , 200, gameController, RANDOM_SEED);

        bot.initialize(gameManager);
    }

    @Test
    public void selectRandomValuesValidInputReturnsRandomlySelectedList() {
        List<String> list = List.of("A", "B", "C", "D", "E", "F", "G");
        int count = 3;
        List<String> randomList = bot.selectRandomValues(list, count);
        assertEquals(count, randomList.size());
        assertTrue(list.containsAll(randomList));
    }

    @Test
    public void selectRandomValuesListNotBigEnoughThrowsIllegalArgumentException() {
        List<String> list = List.of("A", "B");
        int count = 3;
        assertThrows(IllegalArgumentException.class, () -> bot.selectRandomValues(list, count));
    }

    @Test
    public void selectRandomValueValidInputReturnsRandomlySelectedValue() {
        List<String> list = List.of("A", "B", "C", "D", "E", "F", "G");
        String randomValue = bot.selectRandomValue(list, true);
        assertTrue(list.contains(randomValue));
    }

    @Test
    public void selectRandomValueEmptyListThrowsIllegalArgumentException() {
        List<String> list = new ArrayList<>();
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            bot.selectRandomValue(list, true);
        });
    }

    @Test
    public void initializeValidInputSetsGameManagerAndGameController() {
        assertNotNull(bot.getGameManager());
        assertNotNull(bot.getGameController());
    }

    @Test
    public void selectChampionFromValidInputSelectsChampion() {

        List<Champion> champions = List.of(
            new Champion(10, ChampionData.ACHILLES),
            new Champion(11, ChampionData.ARES),
            new Champion(12, ChampionData.TERPSICHORE)
        );
        ChampionSelectionMessage
            message = new ChampionSelectionMessage(Identity.REBEL,
            List.of(new ChampionMessage(champions.get(0)),
                new ChampionMessage(champions.get(1)),
                new ChampionMessage(champions.get(2))));
        bot.selectChampionFrom(message);
        assertNotNull(bot.getChampion());
    }

    @Test
    public void highlightTest() {
        HighlightMessage message = HighlightMessage.builder()
            .cardCount(List.of(0, 1))
            .cardIds(List.of(1005, 1036, 1055, 1088))
            .cardPlayerConditions(List.of(new PlayerCondition(), new PlayerCondition(),
                new PlayerCondition(), new PlayerCondition()))
            .activateEndTurn(true)
            .build();
        PickRequest pickRequest = new PickRequest(bot, gameManager, message);
        bot.highlight(pickRequest);
        assertFalse(bot.wantTurnEnd(message));
        assertNotNull(pickRequest.getSelectedCards());
        assertNull(pickRequest.getSelectedPlayers());
        assertNull(pickRequest.getSelectedActiveSkill());


        pickRequest = new PickRequest(bot2, gameManager, message);
        bot2.highlight(pickRequest);
        assertTrue(bot.wantTurnEnd(message));
        assertNull(pickRequest.getSelectedCards());
        assertNull(pickRequest.getSelectedPlayers());
        assertNull(pickRequest.getSelectedActiveSkill());
    }


}





