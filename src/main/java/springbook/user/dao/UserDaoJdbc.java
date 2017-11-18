package springbook.user.dao;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import springbook.user.domain.Level;
import springbook.user.domain.User;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;

@Data
@Component("userDao")
public class UserDaoJdbc implements UserDao {

    JdbcTemplate jdbcTemplate;

    RowMapper<User> mapper = ( rs , num ) -> new User( rs.getString("id")
                        , rs.getString("name")
                        , rs.getString("password")
                        , Level.valueOf( rs.getInt("level"))
                        , rs.getInt("login")
                        , rs.getInt("recommend"));

    public void add(User user) {
        jdbcTemplate.update(
                "insert into users( " +
                        "id , name , password , level , login , recommend" +
                        " ) values( ? , ? , ? ,  ? , ? , ? )"
                , user.getId() , user.getName(),user.getPassword()
                , user.getLevel().getValue() , user.getLogin() , user.getRecommend());
    }

    public User get( String id ) {
        return jdbcTemplate.queryForObject("select * from users where id = ?"
                , new Object[] { id }
                , this.mapper );
    }

    public void deleteAll() {
        jdbcTemplate.update("delete from users");
    }

    public int getCount() {
        return jdbcTemplate.query( c -> c.prepareStatement("select count(*) from users")
                , rs-> {
                    rs.next();
                    return rs.getInt(1);
                });
    }

    public List<User> getAll(){
        return jdbcTemplate.query("select * from users order by id"
                , this.mapper );
    }

    @Override
    public void update(User user) {
        jdbcTemplate.update(
                "update users set name = ? , password = ? " +
                        ", level = ? , login = ?" +
                        " , recommend = ? where id = ?"
                , user.getName(),user.getPassword()
                , user.getLevel().getValue() , user.getLogin() , user.getRecommend()
                , user.getId() );
    }

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

}
