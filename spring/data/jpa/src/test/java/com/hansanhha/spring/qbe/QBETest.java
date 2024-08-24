package com.hansanhha.spring.qbe;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class QBETest {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void init() {
        userRepository.save(new User("the", "weeknd"));
    }

    @Test
    void qbe() {
        // given
        User userProbe = new User();
        userProbe.setFirstname("the");
        userProbe.setLastname("weeknd");

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnorePaths("id")
                .withMatcher("first_name", ExampleMatcher.GenericPropertyMatchers.exact())
                .withMatcher("last_name", ExampleMatcher.GenericPropertyMatchers.exact());

        Example<User> example = Example.of(userProbe, matcher);

        // when
        List<User> users = userRepository.findAll(example);

        // then
        assertThat(users.size()).isEqualTo(1);
        assertThat(users.getFirst().getFirstname()).isEqualTo(userProbe.getFirstname());
        assertThat(users.getFirst().getLastname()).isEqualTo(userProbe.getLastname());
    }
}
