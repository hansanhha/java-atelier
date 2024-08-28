package com.hansanhha.spring.simple_jpa_repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class SimpleUserServiceTest {

    @Autowired
    private SimpleUserService simpleUserService;

    @BeforeEach
    void init() {
        simpleUserService.save("the", "weenkd", "010-1234-5678");
    }

    @Test
    void findUserTest() {
        long findId = 1L;
        SimpleUser simpleUser = simpleUserService.find(findId);

        assertThat(simpleUser.getId()).isEqualTo(findId);
    }
}