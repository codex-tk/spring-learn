package springbook.user.service;

import lombok.Data;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Data
public class TransactionHandler implements InvocationHandler {
    PlatformTransactionManager transactionManager;
    Object target;
    String pattern;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if ( method.getName().startsWith(pattern)) {
            return invokeInTransaction(proxy,method,args);
        } else {
            return method.invoke(target,args);
        }
    }

    public Object invokeInTransaction(Object proxy, Method method, Object[] args) throws Throwable {
        TransactionStatus tx = transactionManager.getTransaction(new DefaultTransactionAttribute());
        try {
            Object ans = method.invoke(target, args);
            transactionManager.commit(tx);
            return ans;
        } catch (InvocationTargetException e ) {
            transactionManager.rollback(tx);
            throw e.getTargetException();
        }
    }
}
