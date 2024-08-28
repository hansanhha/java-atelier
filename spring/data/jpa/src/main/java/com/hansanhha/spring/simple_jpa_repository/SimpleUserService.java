package com.hansanhha.spring.simple_jpa_repository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class SimpleUserService {

    private final SimpleUserRepository simpleUserRepository;

    public SimpleUserService(SimpleUserRepository simpleUserRepository) {
        this.simpleUserRepository = simpleUserRepository;
    }

    public SimpleUser save(String firstName, String lastName, String phoneNumber) {
        return simpleUserRepository.save(new SimpleUser(firstName, lastName, phoneNumber));
    }

    public SimpleUser find(Long id) {
        return simpleUserRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("not found user"));
    }
}
