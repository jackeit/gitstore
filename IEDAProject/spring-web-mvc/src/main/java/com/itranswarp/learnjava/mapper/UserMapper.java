package com.itranswarp.learnjava.mapper;

import com.itranswarp.learnjava.entity.Diary;
import com.itranswarp.learnjava.entity.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

//@Mapper
@Repository
public interface UserMapper {
    @Select("SELECT * FROM users WHERE id = #{id}")
    User getById(@Param("id") long id);

    @Select("SELECT * FROM USERS WHERE email=#{email}")
    User getByEmail(@Param("email") String email);
    @Select("SELECT * FROM USERS WHERE name=#{name}")
    User getByName(@Param("name") String name);

    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    @Insert("INSERT INTO users (email, password, name, createdAt) " +
            "VALUES (#{user.email}, #{user.password}, #{user.name}, #{user.createdAt})")
    int insert(@Param("user") User user);
    //行不通？
    @Update("create table if not exists `diary`(\n" +
            "`id` bigint(20) not null auto_increment,\n" +
            "`name` varchar(30) not null,\n"+
            "`content` varchar(100) not null,\n" +
            "`date` date not null,\n" +
            "primary key(`id`))\n" +
            "ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;\n")
    void createTable(@Param("tablename") String tablename);

    @Select("SELECT * FROM DIARY WHERE name=#{name}")
    List<Diary> getDiary(@Param("name") String name);

    @Insert("INSERT INTO DIARY (name,content,date,title) VALUES (#{diary.name}" +
            ",#{diary.content},#{diary.date},#{diary.title})")
    int insertDiary(@Param("diary") Diary diary);

    @Select("SELECT * FROM DIARY WHERE id=#{id}")
    Diary getDiaryById(@Param("id") long id);

    @Delete("DELECT * FROM DIARY WHERE id=#{id}")
    int deleteDiaryById(@Param("id") long id);



}


