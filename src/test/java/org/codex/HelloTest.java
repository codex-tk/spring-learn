package org.codex;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.Objects;

public class HelloTest {
    @Test
    public void requireNonNull(){
        Object chk = new Object();
        assertNotNull(Objects.requireNonNull(chk));
    }

    @Test(expected = NullPointerException.class)
    public void nullExcpetion(){
        Object chk = null;
        Objects.requireNonNull(chk);
    }
}
