package com.hansanhha.spring.security.user.repository;

import com.hansanhha.spring.security.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String Email);
}
