package com.mythostrike;

import com.mythostrike.account.service.TokenService;
import com.mythostrike.account.service.UserService;
import com.mythostrike.controller.AuthenticationController;
import com.mythostrike.controller.GameController;
import com.mythostrike.controller.LobbyController;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.simp.SimpMessagingTemplate;

@RequiredArgsConstructor
public class TestConfig {

    private final UserService userService;
    private final TokenService tokenService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @Bean
    public AuthenticationController authenticationController() {
        return new AuthenticationController(userService, tokenService);
    }

    @Bean
    public LobbyController lobbyController() {
        return new LobbyController(userService, simpMessagingTemplate);
    }

    @Bean
    public GameController gameController() {
        return new GameController();
    }

    @Bean
    public UserService userService() {
        return userService;
    }
}
