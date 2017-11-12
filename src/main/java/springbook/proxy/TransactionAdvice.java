package springbook.proxy;

import lombok.Data;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

@Data
public class TransactionAdvice implements MethodInterceptor {
    PlatformTransactionManager transactionManager;
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        TransactionStatus status = this.transactionManager.getTransaction(new DefaultTransactionDefinition());
        try {
            Object ans = invocation.proceed();
            this.transactionManager.commit(status);
            return ans;
        } catch( Exception e ) {
            this.transactionManager.rollback(status);
            throw e;
        }
    }
}
