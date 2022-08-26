package Test;
import Test.User;

import java.util.List;

public interface UserMapper {
    List<User> selectusers();
    void insertuser(User user);
}
