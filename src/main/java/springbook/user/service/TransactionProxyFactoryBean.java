package springbook.user.service;

import lombok.Data;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.lang.Nullable;
import org.springframework.transaction.PlatformTransactionManager;

import java.lang.reflect.Proxy;

@Data
public class TransactionProxyFactoryBean implements FactoryBean<Object> {
    Class<?> objectType;

    Object target;
    PlatformTransactionManager transactionManager;
    String pattern;

    @Nullable
    @Override
    public Object getObject() throws Exception {
        TransactionHandler txHandler = new TransactionHandler();
        txHandler.setTransactionManager(this.transactionManager);
        txHandler.setTarget(this.target);
        txHandler.setPattern(this.pattern);
        Object proxy = Proxy.newProxyInstance(getClass().getClassLoader()
                , new Class[]{objectType}
                , txHandler);
        return proxy;
   }

    @Nullable
    @Override
    public Class<?> getObjectType() {
        return objectType;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }
}
