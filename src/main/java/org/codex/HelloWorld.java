package org.codex;

import lombok.Data;

public class HelloWorld {

    @Data
    public static class LombokObject{
        int iValue;
    }
    public static void main(String[] args) {
        System.out.println("HelloWorld");
    }
}
