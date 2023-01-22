package edu.kit.iti.scale.laralab.controller;

import edu.kit.iti.scale.laralab.model.request.LoginRequest;
import edu.kit.iti.scale.laralab.service.AuthService;
import edu.kit.iti.scale.laralab.service.AuthUserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private static final String LOGIN_FAIL = "Username or password do not match";

    private static final Logger LOG = LoggerFactory.getLogger(AuthController.class);

    private final AuthenticationManager authenticationManager;

    private final AuthUserService authUserService;
    private final AuthService authService;
    private final BCryptPasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest loginRequest) {
        UserDetails userDetails;
        try {
            userDetails = authUserService.loadUserByUsername(loginRequest.username());
        } catch (UsernameNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, LOGIN_FAIL);
        }

        if (passwordEncoder.matches(loginRequest.password(), userDetails.getPassword())) {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password()));
            LOG.debug("Token requested for user: '{}'", authentication.getName());
            return authService.generateToken(authentication);
        }

        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, LOGIN_FAIL);
    }
}
