package springbook;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import static org.junit.Assert.*;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import springbook.user.dao.CountingConnectionMaker;
import springbook.user.dao.DaoFactory;
import springbook.user.dao.SimpleConnectionMaker;
import springbook.user.dao.UserDao;
import springbook.user.domain.User;

import java.sql.SQLException;

@Slf4j
public class UserDaoTest {

    @Test
    public void add() throws SQLException, ClassNotFoundException {

        ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
        UserDao dao = context.getBean("userDao" , UserDao.class);
        dao.deleteAll();
        User user = new User();
        user.setId("0");
        user.setName("Name");
        user.setPassword("Password");

        dao.add(user);

        User fromDao = dao.get("0");
        assertEquals(user,fromDao);
        log.debug("Counter {}" , context.getBean("connectionMaker" , CountingConnectionMaker.class)
                .getCounter());
    }

    @Test
    public void equalTest(){
        DaoFactory factory = new DaoFactory();
        System.out.println( factory.userDao() );
        System.out.println( factory.userDao() );

        ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);

        System.out.println( context.getBean("userDao" , UserDao.class));
        log.debug("Counter {}" , context.getBean("connectionMaker" , CountingConnectionMaker.class)
                .getCounter());
        System.out.println( context.getBean("userDao" , UserDao.class));
        log.debug("Counter {}" , context.getBean("connectionMaker" , CountingConnectionMaker.class)
                .getCounter());
    }
}
