package edu.kit.iti.scale.laralab.repository;

import edu.kit.iti.scale.laralab.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
