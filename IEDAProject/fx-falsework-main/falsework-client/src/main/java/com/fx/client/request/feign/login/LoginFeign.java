package com.fx.client.request.feign.login;

import com.fx.client.request.feign.FeignAPI;
import com.fx.server.msg.ObjectRestResponse;
import com.fx.server.util.user.JwtAuthenticationRequest;
import com.fx.server.vo.FrontUser;
import com.fx.server.vo.MenuVO;
import feign.Param;
import feign.RequestLine;

import java.util.List;

/**
 * @description:
 * @className: LoginFeign
 * @author: liwen
 * @date: 2020/8/2 09:47
 */
public interface LoginFeign extends FeignAPI {

    @RequestLine("POST /jwt/token")
    ObjectRestResponse<String> login(JwtAuthenticationRequest request);

    @RequestLine("GET /jwt/invalid")
    ObjectRestResponse<String> logout(JwtAuthenticationRequest request);

    @RequestLine("GET /user/front/info/{token}")
    ObjectRestResponse<FrontUser> getInfo(@Param("token") String token);

    @RequestLine("GET /user/front/menus/{token}")
    List<MenuVO> getMenus(@Param("token") String token);

    @RequestLine("GET /user/front/menus/all/{token}")
    List<MenuVO> getMenuAll(@Param("token") String token);

}
