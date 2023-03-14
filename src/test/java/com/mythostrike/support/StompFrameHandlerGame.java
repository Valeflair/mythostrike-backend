package com.mythostrike.support;

import com.mythostrike.controller.message.game.GameMessage;
import com.mythostrike.controller.message.game.GameMessageType;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.Queue;

@Slf4j
public class StompFrameHandlerGame extends StompSessionHandlerAdapter {

    @Getter
    private final Queue<GameMessage> messages = new LinkedList<>();


    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        log.debug("Connected");
    }

    @Override
    public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload,
                                Throwable exception) {
        log.error("Got an exception", exception);
    }

    @Override
    public Type getPayloadType(StompHeaders headers) {
        return GameMessage.class;
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        GameMessage message = (GameMessage) payload;

        //ignore log messages, they are not relevant for the tests. Don't add them to the queue.
        if (message.messageType() == GameMessageType.LOG) {
            return;
        }
        messages.add(message);
        log.debug("Received : " + message);
    }

    /**
     * Get the next message in the queue. Throws an exception if the queue is empty.
     * Remove the message from the queue.
     *
     * @return the next message in the queue
     */
    public GameMessage getNextMessage() {
        return messages.remove();
    }

    public boolean containsType(GameMessageType type) {
        return messages.stream().anyMatch(message -> message.messageType() == type);
    }
}
