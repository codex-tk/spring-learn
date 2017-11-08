package springbook.user.dao;

import lombok.Data;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import springbook.user.domain.User;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;

public interface UserDao {
    void add(User user);
    User get( String id );
    void deleteAll();
    int getCount();
    List<User> getAll();

    void update(User u);
}
