package com.hansanhha.spring.simple_jpa_repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SimpleUserRepository extends JpaRepository<SimpleUser, Long> {

    List<SimpleUser> findByLastName(String lastName);
}
