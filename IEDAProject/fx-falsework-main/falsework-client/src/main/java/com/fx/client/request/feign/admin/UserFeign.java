package com.fx.client.request.feign.admin;

import com.fx.client.request.feign.FeignAPI;
import com.fx.server.entity.User;
import com.fx.server.entity.log.SysLoginInfor;
import com.fx.server.msg.ObjectRestResponse;
import com.fx.server.msg.TableResultResponse;
import feign.Param;
import feign.QueryMap;
import feign.RequestLine;

import java.util.List;
import java.util.Map;

/**
 * @description:
 * @className: TestFeign
 * @author: liwen
 * @date: 2020/4/1 17:31
 */
public interface UserFeign extends FeignAPI {
    @RequestLine("GET /user/page")
    TableResultResponse<User> getPageList(@QueryMap Map<String, Object> map);

    @RequestLine("GET /user/online/list")
    List<SysLoginInfor> getOnlineUsers();

    @RequestLine("POST /user/retreat/{userId}")
    ObjectRestResponse<Boolean>  retreat(@Param("userId") String userId);

    @RequestLine("PUT /user/{id}")
    ObjectRestResponse<Integer> update(@Param("id") Integer id, User user);

    @RequestLine("DELETE /user/{id}")
    ObjectRestResponse<Integer> delete(@Param("id") Integer id);

    @RequestLine("PUT /user/password/{id}")
    ObjectRestResponse<Integer> restPassword(@Param("id") Integer id);

    @RequestLine("POST /user")
    ObjectRestResponse<Integer> add(User user);
}
