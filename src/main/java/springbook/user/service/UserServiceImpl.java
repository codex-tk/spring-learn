package springbook.user.service;

import lombok.Data;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import springbook.user.dao.UserDao;
import springbook.user.domain.Level;
import springbook.user.domain.User;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;

@Data
public class UserServiceImpl implements UserService {
    UserDao userDao;
    DataSource dataSource;
    MailSender mailSender;

    public void upgradeLevels() throws SQLException {
        userDao.getAll().forEach(u->{
            if ( canUpgradeLevel(u)){
                upgradeLevel(u);
            }
        });
    }

    protected void upgradeLevel(User user) {
        user.upgradeLevel();
        userDao.update(user);
        sendUpgradeMail(user);
    }

    private void sendUpgradeMail(User user) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();

        this.mailSender.send(mailMessage);
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

    public List<User> getAll(){
        return userDao.getAll();
    }

    public void update(User u) {
        userDao.update(u);
    }
}
