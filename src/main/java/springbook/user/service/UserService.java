package springbook.user.service;

import lombok.Data;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import springbook.user.dao.UserDao;
import springbook.user.domain.Level;
import springbook.user.domain.User;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@Data
public class UserService {
    UserDao userDao;
    DataSource dataSource;

    public void upgradeLevels() throws SQLException {
        TransactionSynchronizationManager.initSynchronization();
        Connection c = DataSourceUtils.getConnection(dataSource);
        c.setAutoCommit(false);
        try {
            List<User> users = userDao.getAll();
            users.stream().forEach(u->{
                if ( canUpgradeLevel(u)){
                    upgradeLevel(u);
                }
            });
            c.commit();
        } catch ( Exception e ) {
            c.rollback();
            throw e;
        } finally {
            DataSourceUtils.releaseConnection(c,dataSource);
            TransactionSynchronizationManager.unbindResource(dataSource);
            TransactionSynchronizationManager.clearSynchronization();
        }

    }

    protected void upgradeLevel(User user) {
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
