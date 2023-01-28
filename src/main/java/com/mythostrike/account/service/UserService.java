package com.mythostrike.account.service;

import com.mythostrike.account.repository.User;
import com.mythostrike.account.repository.UserRepository;
import com.mythostrike.controller.message.authentication.UserAuthRequest;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public User getUser(String username) throws EntityNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(EntityNotFoundException::new);
    }

    public void createUser(UserAuthRequest request) {
        if (userRepository.findByUsername(request.username()).isPresent()) {
            throw new EntityExistsException("Username already used.");
        }
        userRepository.save(new User(request.username(), passwordEncoder.encode(request.password())));
    }

    public boolean areCredentialsValid(UserAuthRequest request) {
        Optional<User> optionalUser = userRepository.findByUsername(request.username());
        if (optionalUser.isEmpty()) {
            return false;
        }
        return passwordEncoder.matches(request.password(), optionalUser.get().getPassword());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            User user = getUser(username);
            return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                Collections.emptyList());
        } catch (EntityNotFoundException e) {
            throw new UsernameNotFoundException(username);
        }
    }

}
