package mockito.user;

public interface UserRepository {

    User findById(String id);
    void saveUser(User user);
}
