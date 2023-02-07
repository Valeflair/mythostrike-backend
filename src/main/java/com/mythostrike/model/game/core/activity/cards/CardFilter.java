package com.mythostrike.model.game.core.activity.cards;

import com.mythostrike.model.game.core.activity.Card;

import java.util.ArrayList;
import java.util.List;

public class CardFilter {

    

    private final List<String> includeList = new ArrayList<>();
    private final List<String> excludeList = new ArrayList<>();

    public void addIncludeFilter(String name) {
        includeList.add(name);
    }

    public void addExcludeFilter(String name) {
        excludeList.add(name);
    }

    public List<Card> filter(List<Card> cards) {
        List<Card> result = new ArrayList<Card>();
        for (Card card : cards) {
            String name = card.getName();
            if (includeList.isEmpty() || includeList.contains(name)) {
                if (excludeList.isEmpty() || !excludeList.contains(name)) {
                    result.add(card);
                }
            }
        }
        return result;
    }
}
