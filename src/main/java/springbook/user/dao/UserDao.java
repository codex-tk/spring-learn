package springbook.user.dao;

import lombok.Data;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import springbook.user.domain.User;

import javax.sql.DataSource;
import java.sql.*;

@Data
public class UserDao {

    JdbcTemplate jdbcTemplate;

    public void add(User user) throws SQLException {
        jdbcTemplate.update(
                "insert into users( id , name , password ) values( ? , ? , ?)"
                , user.getId() , user.getName(),user.getPassword());
    }

    public User get( String id ) throws SQLException {
        return jdbcTemplate.queryForObject("select * from users where id = ?"
                , new Object[] { id }
                , ( rs , num ) -> new User( rs.getString("id")
                        , rs.getString("name")
                        , rs.getString("password"))
        );
    }

    public void deleteAll() throws SQLException {
        jdbcTemplate.update("delete from users");
    }

    public int getCount() throws SQLException {
        return jdbcTemplate.query( c -> c.prepareStatement("select count(*) from users")
                , rs-> {
                    rs.next();
                    return rs.getInt(1);
                });
    }


}
