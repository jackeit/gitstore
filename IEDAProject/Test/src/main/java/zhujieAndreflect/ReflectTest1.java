package zhujieAndreflect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class ReflectTest1 {
    public static void main(String[] args) throws Exception{
        Class<RTest> test = RTest.class;
//        System.out.println(test.getDeclaredAnnotations());
        Constructor<RTest> constructor = test.getConstructor(String.class,int.class);
        RTest rtest = constructor.newInstance("bob",88);
        for(Method i:test.getDeclaredMethods()){
            System.out.println("Method:"+i+"\n");
            for(Annotation k:i.getAnnotations()){
                System.out.println(k+"\n");
            }
        }
//        System.out.println(test.getConstructor(String.class,int.class).isAnnotationPresent(TFirst.class));
        for(Parameter i : test.getConstructor(String.class,int.class).getParameters()){
            System.out.println("--------");
            System.out.println(i.getName());
            System.out.println("--------");
        }
        for(Method i:test.getDeclaredMethods()){
            if(i.isAnnotationPresent(Trun.class)){
                System.out.println("调用get_info:");
                //对Method实例调用invoke就相当于调用该方法，invoke的第一个参数是类实例（即对象，而非Class,Class没有实例化）!!!
                // 即在哪个实例上调用该方法，后面的可变参数要与方法参数一致，否则将报错。
                System.out.println((String)i.invoke(rtest));
            }
        }


    }
}
class RTest{
    private int age;
    private String name;
    @TFirst
    public RTest(String name,int age){
        this.age = age;
        this.name = name;
    }
    @Trun
    public String get_info(){
        return "name:"+this.name+"\n"+"age:"+this.age;
    }
}
