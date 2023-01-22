package edu.kit.iti.scale.laralab.service;

import edu.kit.iti.scale.laralab.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class AuthenticationUserDetailsService implements UserDetailsService {

    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            User user = userService.readUserByUsername(username);
            return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                    Collections.emptyList());
        } catch (EntityNotFoundException e) {
            throw new UsernameNotFoundException(username);
        }
    }
}
