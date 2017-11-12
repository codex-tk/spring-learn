package springbook.proxy;

import lombok.AllArgsConstructor;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.support.NameMatchMethodPointcut;
import org.springframework.util.PatternMatchUtils;

public class NameMatchClassMethodPointcut extends NameMatchMethodPointcut {
    public void setMappedClassName( String mappedName ) {
        this.setClassFilter(new SimpleClassFilter(mappedName));
    }

    @AllArgsConstructor
    private static class SimpleClassFilter implements ClassFilter {
        private String mappedName;

        @Override
        public boolean matches(Class<?> clazz) {
            return PatternMatchUtils.simpleMatch(mappedName,clazz.getSimpleName());
        }
    }
}
