package springbook;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
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
public class TestApplicationContext {

    @Bean
    public DataSource dataSource() {
        SimpleDriverDataSource simpleDriverDataSource = new SimpleDriverDataSource();
        simpleDriverDataSource.setDriverClass(com.mysql.jdbc.Driver.class);
        simpleDriverDataSource.setUrl("jdbc:mysql://192.168.1.10:3306/spring");
        simpleDriverDataSource.setUsername("spring");
        simpleDriverDataSource.setPassword("spring");
        return simpleDriverDataSource;
    }
    @Bean
    public JdbcTemplate jdbcTemplate(){
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.setDataSource(dataSource());
        return jdbcTemplate;
    }

    @Bean
    public DataSourceTransactionManager transactionManager() {
        DataSourceTransactionManager manager = new DataSourceTransactionManager(dataSource());
        return manager;
    }

    @Bean
    public MailSender mailSender() {
        return new DummyMailSender();
    }

    @Bean
    public UserService testUserService(){
        UserServiceImpl service = new UserServiceTest.TestUserServiceImpl("4");
        return service;
    }
}
