package com.chenyongxi.Test1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
public class AppConfig {

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        User user = context.getBean(User.class);
        System.out.println(user.readme.getLogo());
        for(String i:user.favorite.getNames()){
            PrintDebug printDebug = new PrintDebug();
            printDebug.test1();
            System.out.println(i);

        }

    }


}
class PrintDebug{
    private Logger logger = LoggerFactory.getLogger(getClass());
    public void test1(){
        logger.info("hello{}","world");
    }

}

