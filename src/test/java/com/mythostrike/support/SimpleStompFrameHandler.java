package com.mythostrike.support;

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
public class SimpleStompFrameHandler<T> extends StompSessionHandlerAdapter {

    private final Type payloadType;

    @Getter
    private final Queue<T> messages = new LinkedList<>();

    public SimpleStompFrameHandler(Type payloadType) {
        this.payloadType = payloadType;
    }


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
        return payloadType;
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        T message = (T) payload;
        messages.add(message);
        log.debug("Received : " + message);
    }
}
