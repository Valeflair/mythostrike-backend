package com.mythostrike.model.game.player;


import com.mythostrike.controller.GameController;
import com.mythostrike.controller.message.game.HighlightMessage;
import com.mythostrike.controller.message.game.PlayerCondition;
import com.mythostrike.controller.message.lobby.ChampionMessage;
import com.mythostrike.controller.message.lobby.ChampionSelectionMessage;
import com.mythostrike.model.game.activity.Activity;
import com.mythostrike.model.game.activity.system.PickRequest;
import com.mythostrike.model.game.management.GameManager;
import com.mythostrike.model.lobby.Identity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BotTest {

    private final String TEST_BOT_NAME = "PlaceHolderBot";
    private final Random RANDOM_SEED = new Random(50);
    @Mock
    private GameManager gameManager;
    @Mock
    private GameController gameController;
    private Bot placholderBot;
    private Bot randomBot;

    @BeforeEach
    public void setUp() {

        placholderBot = new PlaceholderBot(TEST_BOT_NAME);
        randomBot = new RandomBot("Random Bot");

        gameController = mock(GameController.class);
        gameManager = mock(GameManager.class);
        when(gameManager.getRandom()).thenReturn(RANDOM_SEED);
        when(gameManager.getGameController()).thenReturn(gameController);
        LinkedList mockedLinkedList = mock(LinkedList.class);
        when(gameManager.getCurrentActivity()).thenReturn(mockedLinkedList);
        when(gameManager.getCurrentActivity().remove(any(Activity.class))).thenReturn(true);

        placholderBot.initialize(gameManager);
        randomBot.initialize(gameManager);
    }

    @Test
    public void selectRandomValuesValidInputReturnsRandomlySelectedList() {
        List<String> list = List.of("A", "B", "C", "D", "E", "F", "G");
        int count = 3;
        List<String> randomList = placholderBot.selectRandomValues(list, count);
        assertEquals(count, randomList.size());
        assertTrue(list.containsAll(randomList));
    }

    @Test
    public void selectRandomValuesListNotBigEnoughThrowsIllegalArgumentException() {
        List<String> list = List.of("A", "B");
        int count = 3;
        assertThrows(IllegalArgumentException.class, () -> placholderBot.selectRandomValues(list, count));
    }

    @Test
    public void selectRandomValueValidInputReturnsRandomlySelectedValue() {
        List<String> list = List.of("A", "B", "C", "D", "E", "F", "G");
        String randomValue = placholderBot.selectRandomValue(list, false);
        assertTrue(list.contains(randomValue));
    }

    @Test
    public void selectRandomValueEmptyListThrowsIllegalArgumentException() {
        List<String> list = new ArrayList<>();
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            placholderBot.selectRandomValue(list, true);
        });
    }

    @Test
    public void initializeValidInputSetsGameManagerAndGameController() {
        assertNotNull(placholderBot.getGameManager());
        assertNotNull(placholderBot.getGameController());
    }

    @Test
    public void selectChampionFromValidInputSelectsChampion() {
        ChampionList championList = ChampionList.getChampionList();

        ChampionSelectionMessage
            message = new ChampionSelectionMessage(Identity.REBEL,
            List.of(new ChampionMessage(championList.getChampion(4)),
                new ChampionMessage(championList.getChampion(2)),
                new ChampionMessage(championList.getChampion(1))));
        placholderBot.selectChampionFrom(message);
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
        PickRequest pickRequest = new PickRequest(placholderBot, gameManager, message);
        placholderBot.highlight(pickRequest);
        assertTrue(placholderBot.wantTurnEnd(message));
        assertNull(pickRequest.getSelectedCards());
        assertNull(pickRequest.getSelectedPlayers());
        assertNull(pickRequest.getSelectedActiveSkill());


        pickRequest = new PickRequest(randomBot, gameManager, message);
        randomBot.highlight(pickRequest);
        assertFalse(randomBot.wantTurnEnd(message));
    }


}





