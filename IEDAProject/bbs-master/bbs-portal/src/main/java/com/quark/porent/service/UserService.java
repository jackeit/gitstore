package com.quark.porent.service;

import com.quark.porent.entity.User;

/**
 * @Author ycw
 * Create By 2017/8/24
 */
public interface UserService {

    User getUserByApi(String token);

    
}
