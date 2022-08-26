package Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class Test {
    public static void main(String[] args) {
        Class<User> user= User.class;
//        Method[] methods = user.getDeclaredMethods();
        Field[] fields = user.getFields();
        for (Field i:fields){
            System.out.println(i);
            System.out.println(i.getName());
//            System.out.println(i.getModifiers());
            System.out.println(i.getType());
        }
        System.out.println("end");

    }
}
