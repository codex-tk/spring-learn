package springbook;

import org.springframework.context.annotation.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.mail.MailSender;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.interceptor.TransactionInterceptor;
import springbook.proxy.TransactionAdvice;
import springbook.user.dao.UserDao;
import springbook.user.dao.UserDaoJdbc;
import springbook.user.service.DummyMailSender;
import springbook.user.service.UserService;
import springbook.user.service.UserServiceImpl;

import javax.sql.DataSource;
import java.sql.Driver;
import java.util.Properties;

@Configuration
//@ImportResource( "/applicationContext.xml")
@EnableTransactionManagement
@ComponentScan(basePackages = "springbook.user")
@Import(TestSubApplicationContext.class)
public class AppContext {
    @Configuration
    @Profile("production")
    public static class ProductionContext{
        @Bean
        public MailSender mailSender() {
        return new DummyMailSender();
    }
    }

    @Configuration
    @Profile("test")
    public static class TestContext {
        @Bean
        public UserService testUserService() {
            UserServiceImpl service = new UserServiceTest.TestUserServiceImpl("4");
            return service;
        }
        @Bean
        public MailSender mailSender() {
            return new DummyMailSender();
        }
    }
}
