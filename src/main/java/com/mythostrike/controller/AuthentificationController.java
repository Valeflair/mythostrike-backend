package com.mythostrike.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class AuthentificationController {

    @GetMapping
    public String home(Principal principal) {
        return "Hello, " + principal.getName();
    }
}
