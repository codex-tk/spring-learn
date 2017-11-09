package springbook.user.service;

import springbook.user.domain.User;

import java.sql.SQLException;

public interface UserService {
    void upgradeLevels() throws SQLException;
    void add(User user);
}
