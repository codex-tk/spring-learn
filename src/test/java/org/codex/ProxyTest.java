package org.codex;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInvocation;
import org.junit.Test;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.List;

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

    interface BasicPointCut {
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
    public static class PrefixMatchPointCut implements BasicPointCut {
        String prefix;

        @Override
        public Boolean match(Method method) {
            return method.getName().startsWith(prefix);
        }
    }

    @Data
    @AllArgsConstructor
    public static class BasicAdvisor {
        BasicPointCut pointCut;
        BasicAdvice advice;

        public Object invoke(Object target , Object proxy, Method method, Object[] args) throws Throwable {
            if ( pointCut.match(method)){
                return advice.invoke(new Operation(target,method,args));
            }
            return method.invoke(target,args);
        }
    }


    @Data
    @AllArgsConstructor
    public static class ProxyFactory<T> {
        Class<T> clazz;
        T object;
        List<BasicAdvisor> advisors;

        @Data
        @AllArgsConstructor
        public static class InvocationHandlerImpl implements InvocationHandler {
            Object object;
            List<BasicAdvisor> advisors;
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                for (BasicAdvisor advisor :advisors ) {
                    if ( advisor.getPointCut().match(method)){
                        return advisor.invoke( object , proxy , method , args );
                    }
                }
                return method.invoke(object,args);
            }
        }

        public T getObject(){
            return (T)Proxy.newProxyInstance(
                    getClass().getClassLoader()
                    , new Class[] { clazz }
                    , new InvocationHandlerImpl(object , advisors ));
        }
    }

    @Test
    public void dynamicProxy1() {
        BasicAdvisor basicAdvisor = new BasicAdvisor( m->m.getName().startsWith("sayH")
                ,  (Operation o) -> {
            Object ans = o.proceed();
            if ( ans instanceof String ) {
                return ((String) ans).toUpperCase();
            }
            return ans;
        });

        Hello helloProxy = new ProxyFactory<Hello>(
                Hello.class
                , new HelloTarget()
                , Arrays.asList(basicAdvisor)
                ).getObject();

        assertEquals( helloProxy.sayHello("tk") , "HELLO TK");
        assertEquals( helloProxy.sayHi("tk") , "HI TK");
        assertEquals( helloProxy.sayThankYou("tk") , "ThankYou tk");
    }

    @Test
    public void advicePointCut(){
        ProxyFactoryBean factoryBean = new ProxyFactoryBean();
        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
        pointcut.setMappedName("sayH*");

        factoryBean.setTarget(new HelloTarget());
        factoryBean.addAdvisor(new DefaultPointcutAdvisor( pointcut ,   new UpperCaseAdvice()));
        Hello helloProxy = (Hello)factoryBean.getObject();

        assertEquals( helloProxy.sayHello("tk") , "HELLO TK");
        assertEquals( helloProxy.sayHi("tk") , "HI TK");
        assertEquals( helloProxy.sayThankYou("tk") , "ThankYou tk");
    }
}
