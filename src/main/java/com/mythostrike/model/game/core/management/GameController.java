package com.mythostrike.model.game.core.management;

import com.mythostrike.model.game.Test.Main;
import com.mythostrike.model.game.core.CardSpace;
import com.mythostrike.model.game.core.Player;
import com.mythostrike.model.game.core.activity.Card;
import com.mythostrike.model.game.skill.Skill;
import com.mythostrike.model.game.skill.events.handle.CardAskHandle;

import java.util.ArrayList;

public class GameController {
    private GameManager gameManager;

    public GameController(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    public boolean askForSkillInvoke(Player player, Skill skill) {
        String hint = "do you want to active Skill" + skill.getName() + "?";
        return Main.askForConfirm(player, hint);
    }

    public boolean askForDiscard(CardAskHandle cardAskHandle) {
        Player player = cardAskHandle.getFrom();
        int count = cardAskHandle.getAmount();
        String reason = cardAskHandle.getReason();
        boolean optional = cardAskHandle.isOptional();
        CardSpace fromSpace = cardAskHandle.getFromSpace();
        ArrayList<Card> cards = askForCard(cardAskHandle);
        if (cards.isEmpty()) {
            System.out.println("drop false");
            return false;
        } else {
            System.out.println("drop true");
            gameManager.getCardManager().throwCard(player, cards, fromSpace, cardAskHandle.getTargetList(), reason);
            return true;
        }
    }

    public ArrayList<Card> askForCard(CardAskHandle cardAskHandle) {
        Player player = cardAskHandle.getFrom();
        int amount = cardAskHandle.getAmount();
        boolean optional = cardAskHandle.isOptional();
        String reason = cardAskHandle.getReason();
        CardSpace fromSpace = new CardSpace();
        for (Card card : cardAskHandle.getFromSpace().getCards()) {
            if (cardAskHandle.getCardData() == null || card.isSame(cardAskHandle.getCardData())) {
                fromSpace.addCard(card);
            }
        }
        ArrayList<Card> cards = Main.askForCard(player, fromSpace, amount, amount, optional, reason);
        String hint = "cards picked:";
        for (Card card : cards) {
            hint += card.toString();
        }
        System.out.println(hint);
        return cards;
    }

    public ArrayList<Player> askForChosePlayer(Player player, ArrayList<Player> targetPlayers, int min, int max,
                                               boolean optional, String reason) {
        return Main.askForChosePlayer(player, targetPlayers, min, max, optional, reason);
    }


    public boolean askForConfirm(Player player, String reason) {
        return Main.askForConfirm(player, reason);
    }
}

