package com.giftcard_app.poc_rest.repositories;

import com.giftcard_app.poc_rest.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
