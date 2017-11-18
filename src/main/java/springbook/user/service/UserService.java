package springbook.user.service;

import springbook.user.domain.User;

import java.sql.SQLException;
import java.util.List;

public interface UserService {
    void upgradeLevels() throws SQLException;
    void add(User user);
    List<User> getAll();
    void update(User u);
}
