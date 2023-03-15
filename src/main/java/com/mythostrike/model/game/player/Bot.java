package com.mythostrike.model.game.player;

import com.mythostrike.MythostrikeBackendApplication;
import com.mythostrike.controller.GameController;
import com.mythostrike.controller.message.game.HighlightMessage;
import com.mythostrike.controller.message.game.PlayerCondition;
import com.mythostrike.controller.message.lobby.ChampionSelectionMessage;
import com.mythostrike.model.game.activity.system.PickRequest;
import com.mythostrike.model.game.management.GameManager;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.lang.Thread.sleep;

@Getter
@Slf4j
public abstract class Bot extends Player {

    protected static final int DELAY_BEFORE_ACTION = MythostrikeBackendApplication.TEST_MODE ? 0 : 500;

    protected static final int NO_SKILL_PICKED = -1;

    //TODO: add extra difficulty (normal, cheater)

    protected GameManager gameManager;

    protected GameController gameController;


    protected Bot(String name) {
        super(name);
    }

    /**
     * selects count objects of type T from the list with the RANDOM_SEED from the Game Class
     *
     * @param list  the list to choose from
     * @param count the number of objects to randomly select
     * @return the radomly selected list of objects
     * @throws IllegalArgumentException if the list is not big enough
     */
    protected <T> List<T> selectRandomValues(List<T> list, int count) {
        List<T> tempList = new ArrayList<>(list);
        List<T> returnList = new ArrayList<>();
        int index;
        for (int i = 0; i < count; i++) {
            if (tempList.isEmpty()) {
                throw new IllegalArgumentException("can't select " + count + "values from list");
            }
            index = gameManager.getRandom().nextInt(tempList.size());
            returnList.add(tempList.remove(index));
        }
        return returnList;
    }

    /**
     * selects one random object of type T from the list with the RANDOM_SEED from the Game Class
     *
     * @param list   the list to select the object from
     * @param remove removes the selected item from the list before returning
     * @return the random selected object
     */
    protected <T> T selectRandomValue(List<T> list, boolean remove) {
        if (list.isEmpty()) {
            throw new IllegalArgumentException("can't select a value from an empty list");
        }
        int index = gameManager.getRandom().nextInt(list.size());
        if (remove) {
            return list.remove(index);
        } else {
            return list.get(index);
        }

    }

    @Override
    public void initialize(GameManager gameManager) {
        this.gameManager = gameManager;
        this.gameController = gameManager.getGameController();
    }

    /**
     * Call this method to select a champion from the given message for this bot.
     * Depending on the difficulty of the bot, this method will be implemented differently.
     *
     * @param message the champion selection message to select a champion from
     */
    public abstract void selectChampionFrom(ChampionSelectionMessage message);


    /**
     * Call this method to let the bot highlight a card or skill from the given pick request.
     * Depending on the difficulty of the bot, this method will be implemented differently.
     *
     * @param pickRequest the pick request to tell the bot what he can do
     */
    public void highlight(PickRequest pickRequest) {
        try {
            sleep(DELAY_BEFORE_ACTION);
        } catch (InterruptedException e) {
            //ignore
            log.warn("Interrupted while waiting for action delay");
        }

        HighlightMessage message = pickRequest.getHighlightMessage();


        if (wantTurnEnd(message)) {
            gameManager.endTurn(getUsername());
            return;
        }


        List<Integer> pickedCardIds = new ArrayList<>();
        int pickedSkillId = NO_SKILL_PICKED;
        PlayerCondition playerCondition = new PlayerCondition();
        List<String> pickedPlayers = new ArrayList<>();
        boolean selectedCards = false;
        boolean selectedSkill = false;

        boolean canSelectCards = !message.cardIds().isEmpty() && !message.cardCount().isEmpty();
        boolean canSelectSkill = !message.skillIds().isEmpty() && !message.skillCount().isEmpty();
        boolean canSelectNoCards = message.cardIds().isEmpty() && message.cardCount().contains(0);

        //select cards if possible
        if (canSelectCards) {
            pickedCardIds = pickRandomCards(message.cardIds(), message.cardCount());

            //get player condition if only one card is selected
            if (pickedCardIds.size() == 1) {
                List<PlayerCondition> list = message.cardPlayerConditions();
                if (!list.isEmpty()) {
                    playerCondition = list.get(message.cardIds().indexOf(pickedCardIds.get(0)));
                }
            }
            selectedCards = true;
            //otherwise select a skill
        } else if (canSelectSkill) {

            pickedSkillId = pickRandomSkill(message.skillIds(), message.skillCount());

            //get player condition if a skill was selected
            if (pickedSkillId != NO_SKILL_PICKED) {
                List<PlayerCondition> list = message.skillPlayerConditions();
                if (!list.isEmpty()) {
                    playerCondition = list.get(message.skillIds().indexOf(pickedSkillId));
                }
                //TODO: change to real skillId not index
                pickedSkillId = message.skillIds().indexOf(pickedSkillId);
            }
            selectedSkill = true;
            //select nothing if possible
        } else if (canSelectNoCards) {
            pickedCardIds = new ArrayList<>();
            selectedCards = true;
        }

        //select players if needed
        if (!playerCondition.players().isEmpty() && !playerCondition.count().isEmpty()) {
            pickedPlayers.addAll(pickRandomPlayers(playerCondition.players(), playerCondition.count()));
        }

        //pickRequest finished, remove the acitivy from work stack
        gameManager.getCurrentActivity().remove(pickRequest);

        //send selection
        if (selectedCards) {
            gameManager.selectCards(getUsername(), pickedCardIds, pickedPlayers);
        } else if (selectedSkill) {
            gameManager.selectSkill(getUsername(), pickedSkillId, pickedPlayers);
        } else if (gameManager.getGame().getCurrentPlayer().equals(this)) {
            log.debug("bot " + getUsername() + " selected nothing, ending turn now");
            gameManager.endTurn(getUsername());
        }
    }

    /**
     * select a list of card ids. The size of the list has to be in the cardCount list to be valid.
     *
     * @param cardIds   the list of card ids to select from
     * @param cardCount the list of the amount of cards possibly to select
     * @return the selected card id list
     */
    protected abstract boolean wantTurnEnd(HighlightMessage message);

    /**
     * select a list of card ids. The size of the list has to be in the cardCount list to be valid.
     *
     * @param cardIds   the list of card ids to select from
     * @param cardCount the list of the amount of cards possibly to select
     * @return the selected card id list
     */
    protected abstract List<Integer> pickRandomCards(List<Integer> cardIds, List<Integer> cardCount);

    /**
     * select a skill id from the list. The amount of skills to selected has to be from the skillCount list.
     *
     * @param skillIds   the list of skill ids to select from
     * @param skillCount the list of the amount of skills possibly to select
     * @return the selected skill. If no skill is selected NO_SKILL_PICKED will be returned.
     */
    protected abstract int pickRandomSkill(List<Integer> skillIds, List<Integer> skillCount);

    /**
     * select a list of player usernames. The size of the list has to be in the playerCount list to be valid.
     *
     * @param players     the list of usernames to select from
     * @param playerCount the list of the amount of players possibly to select
     * @return the selected username list
     */
    protected abstract List<String> pickRandomPlayers(List<String> players, List<Integer> playerCount);

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        if (!super.equals(obj)) return false;

        Bot bot = (Bot) obj;

        return this.getUsername().equals(bot.getUsername()) && Objects.equals(gameManager, bot.gameManager);
    }
}
