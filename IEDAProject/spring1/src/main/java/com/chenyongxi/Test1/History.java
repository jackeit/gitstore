package com.chenyongxi.Test1;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Component
public class History {
    @PostConstruct
    public void init(){
        System.out.println("hello");
    }
    @PreDestroy
    //要手动调用applicationContext.close()不能直接点结束进程,否则不会调用
    public void shutdown(){
        System.out.println("end");
    }

    private String[] time = {"2021-04-02","2022-09-08"};
    private String[] goods = {"baskitball","football"};

    public String[] getTime() {
        return time;
    }

    public void setTime(String[] time) {
        this.time = time;
    }

    public String[] getGoods() {
        return goods;
    }

    public void setGoods(String[] goods) {
        this.goods = goods;
    }

}
