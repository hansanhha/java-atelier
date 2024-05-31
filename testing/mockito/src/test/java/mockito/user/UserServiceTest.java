package mockito.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    /**
     * UserService : 테스트 대상(SUT)
     * UserRepository : UserService가 의존하는 객체(DOC)
     * @Mock : DOC 객체를 목(Mock)으로 생성
     * @InjectMocks : 테스트 대상(SUT)에 @Mock으로 생성한 Mock 객체 주입
     */

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;


    @DisplayName("회원 가입")
    @Test
    void join() {
        // stub과 dummy object 지정(Fixture setup phase)
        doNothing()
                .when(userRepository)
                .saveUser(any(User.class));

        userService.saveUser(createUser());
    }

    @DisplayName("회원 조회")
    @Test
    void findUser() {
        // stub 지정(Fixture setup phase)
        var user = createUser();
        doReturn(user)
                .when(userRepository)
                .findById(user.getId());

        // 테스트 대상 메서드 실행(Exercise SUT phase)
        var foundUser = userService.getUser(user.getId());

        // mock 객체 검증(indirect input verify)
        verify(userRepository, times(1)).findById(any(String.class));
        verify(userRepository).findById(same(user.getId()));

        // 결과 검증(Result Verification phase)
        Assertions.assertEquals(user.getId(), foundUser.getId());
        Assertions.assertEquals(user.getName(), foundUser.getName());
    }

    private User createUser() {
        return new User("1", "unit man");
    }

}