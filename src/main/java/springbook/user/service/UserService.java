package springbook.user.service;

import org.springframework.transaction.annotation.Transactional;
import springbook.user.domain.User;

import java.sql.SQLException;
import java.util.List;

@Transactional
public interface UserService {
    void upgradeLevels() throws SQLException;
    void add(User user);

    @Transactional(readOnly = true)
    List<User> getAll();
    void update(User u);
}
