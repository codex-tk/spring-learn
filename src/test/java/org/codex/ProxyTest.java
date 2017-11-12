package org.codex;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.aopalliance.intercept.MethodInvocation;
import org.junit.Test;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

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
        assertEquals( helloProxy.sayHi("tk") , "HI TK");
        assertEquals( helloProxy.sayThankYou("tk") , "THANKYOU TK");
    }

    public static class UpperCaseAdvice implements org.aopalliance.intercept.MethodInterceptor {
        @Override
        public Object invoke(MethodInvocation invocation) throws Throwable {
            String ans = (String)invocation.proceed();
            return ans.toUpperCase();
        }
    }

    @Test
    public void proxyFactoryBean(){
        ProxyFactoryBean pfBean = new ProxyFactoryBean();
        pfBean.setTarget(new HelloTarget());
        pfBean.addAdvice(new UpperCaseAdvice());

        Hello helloProxy = (Hello) pfBean.getObject();

        assertEquals( helloProxy.sayHello("tk") , "HELLO TK");
        assertEquals( helloProxy.sayHi("tk") , "HI TK");
        assertEquals( helloProxy.sayThankYou("tk") , "THANKYOU TK");
    }

    interface PointCut {
        Boolean match( Method method );
    }

    interface BasicAdvice{
        Object invoke( Operation op ) throws InvocationTargetException, IllegalAccessException;
    }

    @Data
    @AllArgsConstructor
    public static class Operation {
        Object object;
        Method method;
        Object[] args;

        public Object proceed() throws InvocationTargetException, IllegalAccessException {
            return method.invoke(object,args);
        }
    }

    @Data
    @AllArgsConstructor
    public static class PrefixMatchPointCut implements PointCut {
        String prefix;

        @Override
        public Boolean match(Method method) {
            return method.getName().startsWith(prefix);
        }
    }

    @Data
    @AllArgsConstructor
    public static class ProxyMethdHandler implements InvocationHandler {
        Object object;
        BasicAdvice advice;
        PointCut pointCut;
        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if ( pointCut.match(method)) {
                return advice.invoke(new Operation(object,method,args));
            }
            return method.invoke(object,args);
        }
    }

    @Data
    @AllArgsConstructor
    public static class ProxyFactory<T> {
        Class<T> clazz;
        T object;
        BasicAdvice advice;
        PointCut pointCut;

        public T getObject(){
            return (T)Proxy.newProxyInstance(
                    getClass().getClassLoader()
                    , new Class[] { clazz }
                    , new ProxyMethdHandler(object
                            , advice
                            , pointCut));
        };
    }

    @Test
    public void dynamicProxy1() {
        Hello helloProxy = new ProxyFactory<Hello>(
                Hello.class
                , new HelloTarget()
                ,  (Operation o) -> {
                    Object ans = o.proceed();
                    if ( ans instanceof String ) {
                        return ((String) ans).toUpperCase();
                    }
                    return ans;
                }
                , m->m.getName().startsWith("say")).getObject();
//
//
//                (Hello) Proxy.newProxyInstance(
//                getClass().getClassLoader()
//                , new Class[] { Hello.class }
//                , new UpperCaseHandler1(
//                        new HelloTarget()
//                        , (Operation o) -> {
//                            Object ans = o.proceed();
//                            if ( ans instanceof String ) {
//                                return ((String) ans).toUpperCase();
//                            }
//                            return ans;
//                        }
//                        , m->m.getName().startsWith("say")));
//                        //, new PrefixMatchPointCut( "say" ) ));

        assertEquals( helloProxy.sayHello("tk") , "HELLO TK");
        assertEquals( helloProxy.sayHi("tk") , "HI TK");
        assertEquals( helloProxy.sayThankYou("tk") , "THANKYOU TK");
    }
}
