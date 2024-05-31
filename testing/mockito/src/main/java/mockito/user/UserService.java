package mockito.user;

public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUser(String id) {
        return userRepository.findById(id);
    }

    public void saveUser(User user) {
        userRepository.saveUser(user);
    }
}
