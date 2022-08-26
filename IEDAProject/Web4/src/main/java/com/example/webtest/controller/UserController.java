package com.example.webtest.controller;

import Test.User;
import Test.UserMapper;
import com.example.webtest.GetMapping;
import com.example.webtest.PostMapping;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.InputStream;
import java.util.List;

public class UserController {

    @GetMapping("/users")
    public List<User> getUser() throws Exception{
        SqlSessionFactory sqlSessionFactory;
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        try(SqlSession sqlSession = sqlSessionFactory.openSession()){
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            return userMapper.selectusers();
        }
    }
    @PostMapping
    public void SetUser(int age,int class_id,String name,String hobbies){

    }

}
