package springbook;

import org.junit.Test;
import org.springframework.aop.Pointcut;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.*;


public class PointCutTest {

    public Object sample(){
        return new Object();
    }

    @Test
    public void AspectJExpressionTest( ) throws NoSuchMethodException {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression("execution(* sample(..))");

        assertThat(pointcut.getClassFilter().matches(PointCutTest.class)
                , is(true));
        assertThat(pointcut.getMethodMatcher().matches(
                PointCutTest.class.getMethod("sample" ) , null ) , is(true)
        );
    }

}
