package springbook;

import org.junit.Test;
import static org.junit.Assert.*;

import springbook.user.dao.SimpleConnectionMaker;
import springbook.user.dao.UserDao;
import springbook.user.domain.User;

import java.sql.SQLException;

public class UserDaoTest {

    @Test
    public void add() throws SQLException, ClassNotFoundException {
        UserDao dao = new UserDao();
        dao.setConnectionMaker(new SimpleConnectionMaker());
        dao.deleteAll();
        User user = new User();
        user.setId("0");
        user.setName("Name");
        user.setPassword("Password");

        dao.add(user);

        User fromDao = dao.get("0");
        assertEquals(user,fromDao);
    }
}
