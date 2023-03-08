package com.mythostrike.model.game.activity.cards;

import java.util.ArrayList;
import java.util.List;

public class CardFilter {

    private final List<String> includeList;
    private final List<String> excludeList;
    private final List<CardSymbol> includeSymbols;
    private final List<CardType> includeTypes;

    public CardFilter() {
        includeList = new ArrayList<>();
        excludeList = new ArrayList<>();
        includeSymbols = new ArrayList<>();
        includeTypes = new ArrayList<>();
    }

    /**
     * @param filter are strings seperated by "|", e.g. "Attack|Defend"
     */
    public CardFilter(String filter) {
        includeList = new ArrayList<>();
        excludeList = new ArrayList<>();
        includeSymbols = new ArrayList<>();
        includeTypes = new ArrayList<>();
        String[] filters = filter.split("\\|");
        for (String include : filters) {
            addIncludeFilter(include);
        }
    }


    public void addIncludeFilter(String name) {
        includeList.add(name);
    }

    public void addIncludeSymbol(CardSymbol symbol) {
        includeSymbols.add(symbol);
    }

    public void addIncludeType(CardType type) {
        includeTypes.add(type);
    }


    public void addExcludeFilter(String name) {
        excludeList.add(name);
    }

    public void removeIncludeFilter(String name) {
        includeList.remove(name);
    }

    public void removeIncludeSymbol(CardSymbol symbol) {
        includeSymbols.remove(symbol);
    }

    public void removeExcludeFilter(String name) {
        includeList.remove(name);
    }

    public boolean match(Card card) {
        String name = card.getName();
        return (includeList.isEmpty() || includeList.contains(name) || includeSymbols.contains(card.getSymbol())) &&
            (excludeList.isEmpty() || !excludeList.contains(name));
    }

    public List<Card> filter(List<Card> cards) {
        if (includeList.isEmpty()) {
            return new ArrayList<>(cards);
        }
        List<Card> result = new ArrayList<>();
        for (Card card : cards) {
            String name = card.getName();
            if (includeList.isEmpty() || includeList.contains(name) || includeSymbols.contains(card.getSymbol())) {
                if (excludeList.isEmpty() || !excludeList.contains(name)) {
                    result.add(card);
                }
            }
        }
        return result;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (String include : includeList) {
            result.append(include).append("|");
        }
        result.delete(result.length() - 1, result.length());
        return result.toString();
    }
}
