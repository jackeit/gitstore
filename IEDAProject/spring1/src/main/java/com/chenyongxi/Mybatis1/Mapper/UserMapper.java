package com.chenyongxi.Mybatis1.Mapper;
import com.chenyongxi.Mybatis1.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface UserMapper {
    @Select("SELECT * FROM test1 WHERE id = #{id}")
    User getById(@Param("id") int id);

    @Select("SELECT * FROM test1")
    List<User> getallUser();

    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    @Insert("INSERT INTO test1 (class_id,age, name, hobbies) VALUES (#{user.class_id}, #{user.age}," +
            " #{user.name}, #{user.hobbies})")
    int insert(@Param("user") User user);
    @Update("create table if not exists `user1`(\n" +
            "`id` bigint(20) not null auto_increment,\n" +
            "`content` varchar(100) not null,\n" +
            "`date` date not null,\n" +
            "primary key(`id`))\n" +
            "ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;\n")
    void createTable(@Param("tablename") String tablename);
}

