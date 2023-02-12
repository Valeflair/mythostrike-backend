package com.mythostrike.controller;

import com.mythostrike.account.repository.User;
import com.mythostrike.account.service.TokenService;
import com.mythostrike.account.service.UserService;
import com.mythostrike.controller.message.authentication.UserAuthRequest;
import com.mythostrike.controller.message.authentication.UserAuthResponse;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Slf4j
public class AuthenticationController {

    private final UserService userService;

    private final TokenService tokenService;

    @PostMapping("/register")
    public ResponseEntity<UserAuthResponse> register(@RequestBody UserAuthRequest request) {
        log.debug("register request: '{}'", request.username());
        try {
            userService.createUser(request);
        } catch (EntityExistsException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already used!");
        }

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(new UserAuthResponse(tokenService.generateToken(request)));
    }

    @PostMapping("/login")
    public ResponseEntity<UserAuthResponse> login(@RequestBody UserAuthRequest request) {
        log.debug("login request: '{}'", request.username());
        if (userService.areCredentialsValid(request)) {
            log.debug("Token requested for user: '{}'", request.username());
            return ResponseEntity.ok(new UserAuthResponse(tokenService.generateToken(request)));
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Wrong username or password!");
    }


    @PostMapping("/data")
    public ResponseEntity<User> getUser(Principal principal) {
        log.debug("user data request: '{}'", principal.getName());
        User user;
        try {
            user = userService.getUser(principal.getName());
        } catch (EntityNotFoundException e) {
            log.error("user not found: '{}'", principal.getName());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!");
        }
        return ResponseEntity.ok(user);
    }

    @GetMapping
    public String home() {
        return "Hello, ";
    }
}
