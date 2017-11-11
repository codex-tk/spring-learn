package org.codex;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.junit.Test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.*;

public class ProxyTest {

    public interface Hello{
        String sayHello( String name );
        String sayHi( String name  );
        String sayThankYou( String name );
    }

    public static class HelloTarget implements Hello {
        public String sayHello(String name) {  return "Hello " + name;  }
        public String sayHi(String name) { return "Hi " + name; }
        public String sayThankYou(String name) { return "ThankYou " + name; }
    }

    @Data
    @AllArgsConstructor
    public static class UpperCaseHandler implements InvocationHandler {
        Hello hello;

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            String ret = (String)method.invoke(hello,args);
            return ret.toUpperCase();
        }
    }

    @Data
    @AllArgsConstructor
    public static class UpperCaseHandler0 implements InvocationHandler {
        Object object;

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            Object result = method.invoke(object,args);
            if ( result instanceof String){
                return ((String)result).toUpperCase();
            }
            return result;
        }
    }
    @Test
    public void dynamicProxy() {
        Hello helloProxy = (Hello) Proxy.newProxyInstance(
                getClass().getClassLoader()
                , new Class[] { Hello.class }
                , new UpperCaseHandler0( new HelloTarget() ));

        assertEquals( helloProxy.sayHello("tk") , "HELLO TK");
    }
}
