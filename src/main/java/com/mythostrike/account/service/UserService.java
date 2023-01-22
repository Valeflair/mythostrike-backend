package edu.kit.iti.scale.laralab.service;

import edu.kit.iti.scale.laralab.model.User;
import edu.kit.iti.scale.laralab.model.request.UserCreateRequest;
import edu.kit.iti.scale.laralab.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public User readUserByUsername(String username) throws EntityNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(EntityNotFoundException::new);
    }

    public void createUser(UserCreateRequest userCreateRequest) {
        if (userRepository.findByUsername(userCreateRequest.username()).isPresent()) {
            throw new RuntimeException("Username already used.");
        }

        userRepository.save(new User(userCreateRequest.username(),
                passwordEncoder.encode(userCreateRequest.password())));
    }

}
