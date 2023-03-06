package com.mythostrike.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public final class WebSocketService {

    private final SimpMessagingTemplate simpMessagingTemplate;

    public void sendMessage(String path, Object payload, String messageName) {
        simpMessagingTemplate.convertAndSend(path, payload);
        ObjectMapper mapper = new ObjectMapper();
        try {
            String json = mapper.writeValueAsString(payload);
            log.debug("{} to '{}': {}", messageName, path, json);
        } catch (JsonProcessingException e) {
            log.error("could not convert {} to json {}", messageName, e);
        }
    }

    public void sendMessageWithoutLog(String path, Object payload) {
        simpMessagingTemplate.convertAndSend(path, payload);
    }
}
