package com.chenyongxi.Mybatis1;

import com.chenyongxi.Mybatis1.Mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Transactional
public class UserService {
    @Autowired
    UserMapper userMapper;

    public User getUserById(int id) {
        // 调用Mapper方法:
        User user = userMapper.getById(id);
        if (user == null) {
            throw new RuntimeException("User not found by id.");
        }
        return user;
    }

    public int insert(User user){
        return userMapper.insert(user);
    }
    public List<User> getallUsers(){
        return userMapper.getallUser();
    }
    public void createtable(String tablename){
        userMapper.createTable(tablename);
    }
}
