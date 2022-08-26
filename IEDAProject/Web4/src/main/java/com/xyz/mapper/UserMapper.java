package com.xyz.mapper;
import com.xyz.mybatistest.User;
import java.util.List;

public interface UserMapper {
    List<User> selectusers();
    void insertuser(User user);
}
