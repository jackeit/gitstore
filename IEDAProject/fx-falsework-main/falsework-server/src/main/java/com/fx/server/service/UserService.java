package com.fx.server.service;

import com.alibaba.druid.util.StringUtils;
import com.fx.server.constant.UserConstant;
import com.fx.server.entity.User;
import com.fx.server.entity.UserInfo;
import com.fx.server.mapper.UserMapper;
import com.fx.server.msg.ObjectRestResponse;
import com.fx.server.msg.TableResultResponse;
import com.fx.server.util.EntityUtils;
import com.fx.server.util.Query;
import com.fx.server.util.user.JwtAuthenticationRequest;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @description:
 * @className: MenuServer
 * @author: liwen
 * @date: 2020/6/30 21:58
 */
@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PermissionService permissionService;

    public TableResultResponse<User> getPageList(Map<String, Object> params) {

        Query query = new Query(params);
        Page<Object> page = PageHelper.startPage(query.getPage(), query.getLimit());
        String keyId = (String) params.get("keyId");
        List<User> list = userMapper.selectPage(StringUtils.isEmpty(keyId) ? null : keyId);
        int total = (int) Math.ceil(page.getTotal() / (float) query.getLimit());
        return new TableResultResponse<User>(total == 0 ? 1 : total, list);
    }


    public ObjectRestResponse<Integer> update(User user) {
        EntityUtils.setCreatAndUpdatInfo(user);
        userMapper.updateByPrimaryKeySelective(user);
        return new ObjectRestResponse<Integer>().rel(true);
    }
    public ObjectRestResponse<Integer> restPassword(Integer id) {
        User user = new User();
        user.setId(id);
        EntityUtils.setUpdatedInfo(user);
        String password = new BCryptPasswordEncoder(UserConstant.PW_ENCORDER_SALT).encode("111111");
        user.setPassword(password);
        userMapper.updateByPrimaryKeySelective(user);
        return new ObjectRestResponse<Integer>().rel(true);
    }

    public ObjectRestResponse<Integer> add(User user) {
        String password = new BCryptPasswordEncoder(UserConstant.PW_ENCORDER_SALT).encode(user.getPassword());
        user.setPassword(password);
        EntityUtils.setCreatAndUpdatInfo(user);
        userMapper.insertSelective(user);
        return new ObjectRestResponse<Integer>().rel(true);
    }

    public ObjectRestResponse<Integer> remove(Integer id) {
        userMapper.deleteByPrimaryKey(id);
        return new ObjectRestResponse<Integer>().rel(true);
    }


    /**
     * 根据用户名获取用户信息
     * @param username
     * @return
     */
    public User getUserByUsername(String username){
        User user = new User();
        user.setUsername(username);
        return userMapper.selectOne(username);
    }

    public UserInfo validate(JwtAuthenticationRequest authenticationRequest){
        return permissionService.validate(authenticationRequest.getUsername(),authenticationRequest.getPassword());
    }


}
