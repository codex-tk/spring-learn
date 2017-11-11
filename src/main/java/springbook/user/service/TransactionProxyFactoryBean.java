package springbook.user.service;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.lang.Nullable;

public class TransactionProxyFactoryBean implements FactoryBean<Object> {
    @Nullable
    @Override
    public Object getObject() throws Exception {
        return null;
    }

    @Nullable
    @Override
    public Class<?> getObjectType() {
        return null;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }
}
