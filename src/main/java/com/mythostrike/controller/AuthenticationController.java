package com.mythostrike.controller;

import com.mythostrike.account.repository.User;
import com.mythostrike.account.service.TokenService;
import com.mythostrike.account.service.UserService;
import com.mythostrike.controller.request.AuthRequest;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Slf4j
public class AuthenticationController {

    private final UserService userService;

    private final TokenService tokenService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody AuthRequest request) {
        log.debug("register request: '{}'", request.username());
        try {
            userService.createUser(request);
        } catch (EntityExistsException e) {
            return ResponseEntity.status(409).body("Username already used!");
        }

        return ResponseEntity.status(201).body(tokenService.generateToken(request));
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody AuthRequest request) {
        log.debug("login request: '{}'", request.username());
        if (userService.areCredentialsValid(request)) {
            log.debug("Token requested for user: '{}'", request.username());
            return ResponseEntity.ok(tokenService.generateToken(request));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Wrong username or password!");
    }


    @PostMapping("/data")
    public ResponseEntity<User> getUser(Principal principal) {
        log.debug("user data request: '{}'", principal.getName());
        User user;
        try {
            user = userService.getUser(principal.getName());
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }

    @GetMapping
    public String home() {
        return "Hello, ";
    }
}
