package mockito.springboot.user;

import org.springframework.stereotype.Repository;

import java.util.HashMap;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final HashMap<String, User> userMap = new HashMap<>();

    @Override
    public User findById(String id) {
        return userMap.get(id);
    }

    @Override
    public void saveUser(User user) {
        userMap.put(user.getId(), user);
    }

}
