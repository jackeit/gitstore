package com.chengyongxi.springmvc.controller;

import com.chengyongxi.springmvc.Entity.User;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;


@RestController
public class UserController {
    private List<String> list;

    @RequestMapping("/user")

    public String printhello(String name){
        System.out.println("name:"+name);
        return "{'name':'chen'}";
    }

    @RequestMapping("/{user}/{id}")

    public String userhello(@PathVariable String user,
                            @PathVariable int id){
        System.out.printf("name:%s,id:%d%n", user,id);
        return String.format("{'name':'%s','id':'%d'}", user,id);
    }
    @RequestMapping("/user/json")

    public String ujson(@RequestBody List<String> list){
        System.out.println(list);
        return "done";
    }
    @RequestMapping("/userinfo")

    public String userinfo(@RequestBody User user){
//        System.out.printf("user.name:%s,user.age:%d",user.getName(),user.getAge());
//        System.out.printf("user.address:%s,user.email:%s",user.getAddress().getLocation(),
//        user.getAddress().getEmail());
        System.out.println(user);
        return "info_up_fdone";
    }
    @RequestMapping("/userdate")

    public String userdate(Date date1,@DateTimeFormat(pattern = "yyyy-MM-dd") Date date2){
        System.out.println("date1:"+date1);
        System.out.println("date2:"+date2);
        return "datedone";
    }
}
