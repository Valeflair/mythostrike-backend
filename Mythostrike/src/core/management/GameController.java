package core.management;

import core.*;
import events.handle.CardAskHandle;
import Test.Main;

import java.util.ArrayList;

public class GameController {
    private GameManager gameManager;

    public GameController(GameManager gameManager) {
        this.gameManager = gameManager;
    }
    public boolean askForSkillInvoke(Player player, Skill skill){
        String hint = "do you want to active Skill" + skill.getName() + "?";
        return Main.askForConfirm(player, hint);
    }
    public boolean askForDiscard(CardAskHandle cardAskHandle) {
        Player player = cardAskHandle.getFrom();
        int count = cardAskHandle.getAmount();
        String reason = cardAskHandle.getReason();
        boolean optional = cardAskHandle.isOptional();
        CardList targetSpace = cardAskHandle.getTargetSpace();
        ArrayList<Card> cards = askForCard(player, targetSpace, count, count, optional, reason);
        if (cards.isEmpty()) {
            return false;
        } else {
            gameManager.getCardManager().throwCard(player, cards, gameManager.getGame().getThrowDeck(), reason);
            return true;
        }
    }
    public ArrayList<Card> askForCard (Player player, CardList targetSpace,int min, int max,
        boolean optional, String reason){
            return Main.askForCard(player, targetSpace, min, max, optional, reason);
        }
    public ArrayList<Player> askForChosePlayer (Player player, ArrayList < Player > targetPlayers,int min, int max,
        boolean optional, String reason){
            return Main.askForChosePlayer(player, targetPlayers, min, max, optional, reason);
        }
    public boolean askForConfirm (Player player, String reason){
            return Main.askForConfirm(player, reason);
        }
}

