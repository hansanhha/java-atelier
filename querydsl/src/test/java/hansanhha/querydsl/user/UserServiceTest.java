package hansanhha.querydsl.user;

import hansanhha.querydsl.user.dto.UserResponse;
import hansanhha.querydsl.user.entity.User;
import hansanhha.querydsl.user.repository.UserRepository;
import hansanhha.querydsl.user.repository.UserRepositoryImpl;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserServiceTest {

    @Autowired
    UserService userService;

    @Test
    @Order(Integer.MIN_VALUE)
    @DisplayName("회원가입")
    void join() {
        String username = "test user";
        UUID userNumber = userService.join(username);
    }

    @Test
    @Order(Integer.MAX_VALUE)
    @DisplayName("유저 정보 조회")
    void getUserInfo() {
        String username = "test user";
        UUID userNumber = userService.join(username);
        UserResponse user = userService.getOne(userNumber);
        System.out.println(user);
    }

}