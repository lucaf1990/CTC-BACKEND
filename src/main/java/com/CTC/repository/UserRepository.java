package com.CTC.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.CTC.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByUserNameOrEmail(String username, String email);

    User findByUserName(String username);
   User findByConfirmationToken(String confirmationToken);

    Boolean existsByUserName(String username);

    Boolean existsByEmail(String email);
}
