package springbook;

import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import springbook.user.dao.olds.DaoFactory;
import springbook.user.dao.UserDao;
import springbook.user.domain.Level;
import springbook.user.domain.User;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;

@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppContext.class)
@ActiveProfiles("production")
public class UserDaoTest {

    @Autowired ApplicationContext context;

    @Autowired UserDao dao;

    List<User> userList = Arrays.asList(
            new User("1", "Name 1", "Password 1" , Level.BASIC , 0 , 0 )
            , new User("2", "Name 2", "Password 2", Level.BASIC , 0 , 0 )
            , new User("3", "Name 3", "Password 3", Level.BASIC , 0 , 0 )
    );

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

        User user0 = userList.get(0);
        User user1 = userList.get(1);

        dao.add(user0);
        dao.add(user1);
        assertEquals( 2 , dao.getCount());

        User userget0 = dao.get(user0.getId());
        User userget1 = dao.get(user1.getId());

        assertEquals(user0 , userget0);
        assertEquals(user1 , userget1);
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

        dao.deleteAll();

        assertEquals(dao.getCount() , 0 );

        IntStream.range(0,2).forEach(i->{
            dao.add(userList.get(i));
            assertEquals(dao.getCount() , i + 1);
        });
    }

    @Test( expected = EmptyResultDataAccessException.class)
    public void getUserFailure() throws SQLException {
        dao.get("invalid");
    }

    @Test
    public void getAll() throws SQLException {
        dao.deleteAll();

        IntStream.range(0,2).forEach(i->{
            dao.add(userList.get(i));
            List<User> userAll = dao.getAll();
            assertEquals(userAll.size() , i + 1);
        });
    }

    @Test(expected= DataAccessException.class)
    public void duplicateKey(){
        User u = userList.get(0);
        User dup = userList.get(0);

        dao.deleteAll();
        dao.add(u);
        dao.add(dup);
    }

    @Test
    public void update(){
        dao.deleteAll();
        dao.add(userList.get(0));
        dao.add(userList.get(1));

        User u = userList.get(0);
        u.setLogin(100);
        u.setName("Sample");

        dao.update( u );
        User updatedUser = dao.get(u.getId());
        assertEquals(u , updatedUser);
        User sameUser = dao.get(userList.get(1).getId());
        assertEquals(userList.get(1) , sameUser);
    }

}
