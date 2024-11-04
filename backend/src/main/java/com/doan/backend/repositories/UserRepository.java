package com.doan.backend.repositories;

import com.doan.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByEmail(String email);
    Optional<User>findById(String id);
    Boolean existsByEmail(String email);
    Optional<User> findByVerificationToken(String token);
}