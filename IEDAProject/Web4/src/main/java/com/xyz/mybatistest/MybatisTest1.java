package com.xyz.mybatistest;

import com.xyz.mapper.UserClassMapper;
import com.xyz.mapper.UserMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MybatisTest1 {
    public static SqlSessionFactory sqlSessionFactory;
    public static void main(String[] args) throws Exception{
        setSqlSessionFactory();
        try(SqlSession sqlSession = sqlSessionFactory.openSession()){
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            UserClassMapper userClassMapper = sqlSession.getMapper(UserClassMapper.class);
            List<UserClassName> userClassName = userClassMapper.getUserClasses();
            List<User> users = userMapper.selectusers();
            Map<Integer, String> classmap = new HashMap<>();
            for(UserClassName i : userClassName){
                classmap.put(i.getId(),i.getClassName());
            }
            for(User i:users){
                i.setClassName(classmap.get(i.getClass_id()));
            }
//            Map<Integer,String> classMap = userClassMapper.getclassHashMap();
            System.out.println("end");
        }


    }
    public static void setSqlSessionFactory() throws Exception{
        String resource = "mybatis-config.xml";
        try(InputStream inputStream = Resources.getResourceAsStream(resource)){
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        }
    }
}
