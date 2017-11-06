package springbook.user.dao;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DaoFactory {
    @Bean
    public UserDao userDao(){
        UserDao dao = new UserDao();
        dao.setConnectionMaker(connectionMaker());
        return dao;
    }

    @Bean
    public ConnectionMaker connectionMaker() {
        CountingConnectionMaker c = new CountingConnectionMaker();
        c.setRealConnectionMaker(realConnectionMaker());
        return c;
    }

    @Bean
    public ConnectionMaker realConnectionMaker() {
        return new SimpleConnectionMaker();
    }
}
