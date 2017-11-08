package springbook.user.dao.olds;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import springbook.user.dao.UserDao;
import springbook.user.dao.UserDaoJdbc;

import javax.sql.DataSource;
import java.sql.SQLException;

@Configuration
public class DaoFactory {
    @Bean
    public UserDao userDao() throws SQLException, ClassNotFoundException {
        UserDaoJdbc dao = new UserDaoJdbc();
        dao.setJdbcTemplate(new JdbcTemplate(dataSource()));
        return dao;
    }

    @Bean
    public JdbcContext jdbcContext() throws ClassNotFoundException, SQLException {
        JdbcContext jdbcContext = new JdbcContext();
        jdbcContext.setDataSource(dataSource());
        return jdbcContext;
    }

    @Bean
    public DataSource dataSource() throws ClassNotFoundException, SQLException {
        SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
        dataSource.setDriverClass(org.sqlite.JDBC.class);
        dataSource.setUrl("jdbc:sqlite:./sample.db");
        return dataSource;
    }
}
