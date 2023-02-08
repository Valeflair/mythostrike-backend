package com.mythostrike.model.game.core.activity.cards;

import com.mythostrike.model.game.core.activity.Card;

import java.util.ArrayList;
import java.util.List;

public class CardFilter {

    private final List<String> includeList;
    private final List<String> excludeList;

    public CardFilter() {
        includeList = new ArrayList<>();
        excludeList = new ArrayList<>();
    }

    /**
     * @param filter are strings seperated by "|", e.g. "Attack|Defend"
     */
    public CardFilter(String filter) {
        includeList = new ArrayList<>();
        excludeList = new ArrayList<>();
        String[] filters = filter.split("\\|");
        for (String include : filters) {
            addIncludeFilter(include);
        }
    }


    public void addIncludeFilter(String name) {
        includeList.add(name);
    }

    public void addExcludeFilter(String name) {
        excludeList.add(name);
    }

    public void removeIncludeFilter(String name) {
        includeList.remove(name);
    }

    public void removeExcludeFilter(String name) {
        includeList.remove(name);
    }

    public List<Card> filter(List<Card> cards) {
        if (includeList.isEmpty()) {
            return new ArrayList<>(cards);
        }
        List<Card> result = new ArrayList<>();
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
