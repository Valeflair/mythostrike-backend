package com.mythostrike.controller.message.game;

import java.util.List;

public record CardMoveMessage(WebsocketGameMessageType messageType, String source, String destination, int count,
                              List<Integer> cardsId) {

    public CardMoveMessage(String source, String destination, int count, List<Integer> cardsId) {
        this(WebsocketGameMessageType.CARD_MOVE, source, destination, count, cardsId);
    }
}
