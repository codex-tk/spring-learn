package springbook.user.service;

import lombok.Data;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import springbook.user.domain.User;

import java.sql.SQLException;

@Data
public class UserServiceTx implements UserService {
    UserService userService;
    PlatformTransactionManager transactionManager;
    public void upgradeLevels() throws SQLException {
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
        try {
            userService.upgradeLevels();
            transactionManager.commit(status);
        } catch ( Exception e ) {
            transactionManager.rollback(status);
            throw e;
        }
    }

    public void add(User user){
        userService.add(user);
    }
}
