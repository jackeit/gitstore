package com.example.web1.Test;

import java.lang.reflect.Constructor;

public class Test1 {
    public static void main(String[] args) throws Exception{
        Class<String> cls = String.class;
        Constructor<String> cons = cls.getConstructor(String.class);
        String k = cons.newInstance("123");
        System.out.println(k);
//        System.out.println(cons.newInstance("123") instanceof String);
        System.out.println(cls.getSuperclass());
        System.out.println(cls);
        System.out.println("".getClass());

    }
}
