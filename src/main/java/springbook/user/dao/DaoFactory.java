package springbook.user.dao;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DaoFactory {
    @Bean
    public UserDao userDao(){
        UserDao dao = new UserDao();
        dao.setConnectionMaker(new SimpleConnectionMaker());
        return dao;
    }
}
