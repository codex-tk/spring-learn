package springbook;

import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import springbook.user.dao.DaoFactory;
import springbook.user.dao.UserDao;
import springbook.user.domain.User;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;

@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "./../applicationContext.xml")
public class UserDaoTest {

    @Autowired ApplicationContext context;

    @Autowired UserDao dao;

    @Before
    public void setUp(){
        //ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
        //ApplicationContext context = new GenericXmlApplicationContext("");
        //dao = context.getBean("userDao" , UserDao.class);
    }

    @Test
    public void addAndGet() throws SQLException, ClassNotFoundException {

        dao.deleteAll();

        assertEquals(0 , dao.getCount());

        User user0 = new User("0" , "Name" , "Password");
        User user1 = new User("1" , "Name1" , "Password1");

        dao.add(user0);
        dao.add(user1);
        assertEquals( 2 , dao.getCount());

        User userget0 = dao.get(user0.getId());
        User userget1 = dao.get(user1.getId());

        assertEquals(userget0.getName() , user0.getName());
        assertEquals(userget0.getPassword() , user0.getPassword());

        assertEquals(userget1.getName() , user1.getName());
        assertEquals(userget1.getPassword() , user1.getPassword());
    }

    @Test
    public void equalTest() throws SQLException, ClassNotFoundException {
        DaoFactory factory = new DaoFactory();
        System.out.println( factory.userDao() );
        System.out.println( factory.userDao() );

        ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);

        System.out.println( context.getBean("userDao" , UserDao.class));
        System.out.println( context.getBean("userDao" , UserDao.class));
    }

    @Test
    public  void count() throws SQLException {
        List<User> userList = Arrays.asList(
                new User("1", "Name 1", "Password 1")
                , new User("2", "Name 2", "Password 2")
                , new User("3", "Name 3", "Password 3")
        );

        dao.deleteAll();

        assertEquals(dao.getCount() , 0 );

        IntStream.range(0,2).forEach(i->{
            try {
                dao.add(userList.get(i));
                assertEquals(dao.getCount() , i + 1);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    @Test( expected = EmptyResultDataAccessException.class)
    public void getUserFailure() throws SQLException {
        dao.get("invalid");
    }

    @Test
    public void getAll() throws SQLException {
        dao.deleteAll();

        List<User> userList = Arrays.asList(
                new User("1", "Name 1", "Password 1")
                , new User("2", "Name 2", "Password 2")
                , new User("3", "Name 3", "Password 3")
        );

        IntStream.range(0,2).forEach(i->{
            try {
                dao.add(userList.get(i));
                List<User> userAll = dao.getAll();
                assertEquals(userAll.size() , i + 1);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }
}
