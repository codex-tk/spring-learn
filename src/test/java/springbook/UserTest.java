package springbook;

import org.junit.Test;
import springbook.user.domain.Level;
import springbook.user.domain.User;

import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.*;

public class UserTest {

    @Test
    public void upgradeLevel(){
        User u = new User();
        u.setLevel(Level.BASIC);

        List<Level> ans = Arrays.asList(Level.SILVER, Level.GOLD, Level.GOLD);

        ans.stream().forEach(l-> {
            u.upgradeLevel();
            assertEquals( u.getLevel() , l );
        } );
    }
}
