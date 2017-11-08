package springbook.user.service;

import lombok.Data;
import springbook.user.dao.UserDao;
import springbook.user.domain.Level;
import springbook.user.domain.User;

import java.util.List;

@Data
public class UserService {
    UserDao userDao;

    public void upgradeLevels(){
        List<User> users = userDao.getAll();

        users.stream().forEach(u->{
            if ( canUpgradeLevel(u)){
                upgradeLevel(u);
            }
        });
    }

    private void upgradeLevel(User user) {
        user.upgradeLevel();
        userDao.update(user);
    }

    public Boolean canUpgradeLevel(User user ){
        switch( user.getLevel() ) {
            case BASIC:
                if ( user.getLogin() >= 50 ) {
                    return true;
                }
                break;
            case SILVER:
                if ( user.getRecommend() >= 30 ) {
                    return true;
                }
                break;
        }
        return false;
    }

    public void add(User user){
        if ( user.getLevel() == null ) user.setLevel(Level.BASIC);
        userDao.add(user);
    }
}
