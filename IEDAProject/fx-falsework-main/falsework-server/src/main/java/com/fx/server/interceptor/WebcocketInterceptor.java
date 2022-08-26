package com.fx.server.interceptor;

import com.fx.server.config.UserConfiguration;
import com.fx.server.entity.log.SysLoginInfor;
import com.fx.server.jwt.IJWTInfo;
import com.fx.server.service.log.SysLoginInfoService;
import com.fx.server.util.user.JwtTokenUtil;
import com.fx.server.constant.CommonConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Date;
import java.util.Map;

import static java.util.UUID.*;

/**
 * @author buhao
 * @version MyInterceptor.java, v 0.1 2019-10-17 19:21 buhao
 */
@Slf4j
@Component
public class WebcocketInterceptor implements HandshakeInterceptor {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private UserConfiguration userConfiguration;

    @Autowired
    private SysLoginInfoService sysLoginInfoService;

    /**
     * 握手前
     *
     * @param request
     * @param response
     * @param wsHandler
     * @param attributes
     * @return
     * @throws Exception
     */
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        log.info("握手开始");

        // 获得请求参数
        String token = request.getHeaders().getFirst(userConfiguration.getUserTokenHeader());
        String userName = request.getHeaders().getFirst("userName");
        IJWTInfo ijwtInfo = jwtTokenUtil.getInfoFromToken(token);
        SysLoginInfor sysLoginInfor = new SysLoginInfor();
        sysLoginInfor.setInfoId(randomUUID().getMostSignificantBits());
        sysLoginInfor.setLoginName(userName);
        sysLoginInfor.setIpaddr(request.getRemoteAddress().toString());
        sysLoginInfor.setLoginTime(new Date());

        if (ijwtInfo != null) {
            sysLoginInfor.setStatus("0");
            sysLoginInfor.setMsg("登录成功！");
            // 放入属性域
            attributes.put(CommonConstants.WEB_SOCKET_USER_LIST, sysLoginInfor);
            attributes.put(CommonConstants.WEB_SOCKET_USER_ID, sysLoginInfor.getLoginName());
            sysLoginInfoService.addSysJobLog(sysLoginInfor);
            return true;
        } else {
            log.error("用户登录已失效");
            sysLoginInfor.setStatus("1");
            sysLoginInfor.setMsg("登录失败！");
            sysLoginInfoService.addSysJobLog(sysLoginInfor);
            return false;
        }

    }

    /**
     * 握手后
     *
     * @param request
     * @param response
     * @param wsHandler
     * @param exception
     */
    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
        log.info("握手完成.");
    }

}