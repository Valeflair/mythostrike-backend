package com.mythostrike.model.game.player;

import com.mythostrike.controller.GameController;
import com.mythostrike.controller.message.game.ChampionSelectionMessage;
import com.mythostrike.controller.message.game.HighlightMessage;
import com.mythostrike.controller.message.game.PlayerCondition;
import com.mythostrike.model.game.Game;
import com.mythostrike.model.game.activity.system.PickRequest;
import com.mythostrike.model.game.management.GameManager;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
public class Bot extends Player {

    //TODO: add diffrent difficulty (placeholder(current), random, normal, cheater)
    private int difficulty;
    private GameManager gameManager;

    private GameController gameController;


    public Bot(String name, int difficulty) {
        super(name);
    }

    @Override
    public void initialize(GameManager gameManager) {
        this.gameManager = gameManager;
        this.gameController = gameManager.getGameController();
    }


    public void selectChampionFrom(ChampionSelectionMessage message) {
        gameManager.selectChampion(getUsername(), message.champions().get(0));
    }

    public void highlight(PickRequest pickRequest) {

        HighlightMessage message = pickRequest.getHighlightMessage();
        if (message.activateEndTurn()) {
            gameManager.endTurn(getUsername());
            return;
        }
        //it does random shit
        List<Integer> pickedCardIds = new ArrayList<>();
        int pickedSkillId = -1;
        PlayerCondition playerCondition = new PlayerCondition();
        List<String> pickedPlayers = new ArrayList<>();
        boolean selectedCards = false;

        //select cards if possible
        if (!message.cardIds().isEmpty() && !message.cardCount().isEmpty()) {
            List<Integer> shuffleCount = new ArrayList<>(message.cardCount());
            List<Integer> shuffleIds = new ArrayList<>(message.cardIds());
            Collections.shuffle(shuffleCount, Game.RANDOM_SEED);
            Collections.shuffle(shuffleIds, Game.RANDOM_SEED);

            for (int i = 0; i < shuffleCount.get(0); i++) {
                pickedCardIds.add(shuffleIds.get(i));
            }
            //get player condition if only one card is selected
            if (pickedCardIds.size() == 1) {
                playerCondition = message.cardPlayerConditions().get(message.cardIds().indexOf(pickedCardIds.get(0)));
            }
            selectedCards = true;
        //otherwise select a skill
        } else if (!message.skillIds().isEmpty() && !message.skillCount().isEmpty()) {
            List<Integer> shuffleCount = new ArrayList<>(message.skillCount());
            List<Integer> shuffleIds = new ArrayList<>(message.skillIds());
            Collections.shuffle(shuffleCount, Game.RANDOM_SEED);
            Collections.shuffle(shuffleIds, Game.RANDOM_SEED);

            //select skill and get player conditions if possible
            if (shuffleCount.get(0) == 1) {
                pickedSkillId = shuffleIds.get(0);
                playerCondition = message.cardPlayerConditions().get(message.cardIds().indexOf(pickedSkillId));
            }
        }

        //select players if needed
        if ( !playerCondition.players().isEmpty() && !playerCondition.count().isEmpty()) {
            List<Integer> shuffleCount = new ArrayList<>(playerCondition.count());
            List<String> shufflePlayers = new ArrayList<>(playerCondition.players());
            Collections.shuffle(shuffleCount, Game.RANDOM_SEED);
            Collections.shuffle(shufflePlayers, Game.RANDOM_SEED);

            for (int i = 0; i < shuffleCount.get(0); i++) {
                pickedPlayers.add(shufflePlayers.get(i));
            }
        }

        gameManager.getCurrentActivity().remove(pickRequest);

        //send selection
        if (selectedCards) {
            gameManager.selectCards(getUsername(), pickedCardIds, pickedPlayers);
        } else {
            gameManager.selectSkill(getUsername(), pickedSkillId, pickedPlayers);
        }


    }
}
