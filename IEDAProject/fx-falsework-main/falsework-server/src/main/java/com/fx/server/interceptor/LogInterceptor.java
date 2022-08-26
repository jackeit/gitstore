package com.fx.server.interceptor;


import com.fx.server.config.UserConfiguration;
import com.fx.server.context.BaseContextHandler;
import com.fx.server.entity.log.LogInfo;
import com.fx.server.filter.RequestWrapper;
import com.fx.server.jwt.IJWTInfo;
import com.fx.server.service.PermissionService;
import com.fx.server.service.log.GateLogService;
import com.fx.server.util.DBLog;
import com.fx.server.util.user.JwtTokenUtil;
import com.fx.server.vo.PermissionInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by ace on 2017/9/10.
 */
public class LogInterceptor extends HandlerInterceptorAdapter {
    private Logger logger = LoggerFactory.getLogger(LogInterceptor.class);
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private UserConfiguration userConfiguration;
    @Autowired
    private GateLogService gateLogService;
    @Autowired
    private PermissionService permissionService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        String token = request.getHeader(userConfiguration.getUserTokenHeader());
        IJWTInfo infoFromToken = jwtTokenUtil.getInfoFromToken(token);


        HttpServletRequest requestWrapper = null;

        //遇到post方法才对request进行包装
        String methodType = request.getMethod();

        List<PermissionInfo> permissionIfs = permissionService.getAllPermission();
        // 判断资源是否启用权限约束
        Stream<PermissionInfo> stream = getPermissionIfs(request.getRequestURI(), request.getMethod().toString(), permissionIfs);
        List<PermissionInfo> result = stream.collect(Collectors.toList());
        PermissionInfo[] permissions = result.toArray(new PermissionInfo[]{});
        if (permissions.length > 0) {
            checkUserPermission(permissions, request, infoFromToken);
        }


        return super.preHandle(request, response, handler);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        BaseContextHandler.remove();
        super.afterCompletion(request, response, handler, ex);
    }

    private boolean checkUserPermission(PermissionInfo[] permissions, HttpServletRequest ctx, IJWTInfo user) {
        List<PermissionInfo> permissionInfos = permissionService.getPermissionByUsername(user.getUniqueName());
        PermissionInfo current = null;
        for (PermissionInfo info : permissions) {
            boolean anyMatch = permissionInfos.parallelStream().anyMatch(permissionInfo -> permissionInfo.getCode().equals(info.getCode()));
            if (anyMatch) {
                current = info;
                break;
            }
        }
        if (current == null) {
            return true;
        } else {
            if (!RequestMethod.GET.toString().equals(current.getMethod())) {
                setCurrentUserInfoAndLog(ctx, user, current);
            }
            return false;
        }
    }

    private void setCurrentUserInfoAndLog(HttpServletRequest request, IJWTInfo user, PermissionInfo pm) {
        String host = request.getRemoteHost();
        if (isJson(request)) {
            // 获取json字符串
            String jsonParam = null;
            try {
                jsonParam = new RequestWrapper(request).getBodyString();
                LogInfo logInfo = new LogInfo(pm.getMenu(), pm.getName(), pm.getUri(), new Date(), user.getId(), user.getName(), host, jsonParam);
                DBLog.getInstance().setLogService(gateLogService).offerQueue(logInfo);
            } catch (IOException e) {
                e.printStackTrace();
            }


        }


    }

    /**
     * 获取目标权限资源
     *
     * @param requestUri
     * @param method
     * @param serviceInfo
     * @return
     */
    private Stream<PermissionInfo> getPermissionIfs(final String requestUri, final String method, List<PermissionInfo> serviceInfo) {
        return serviceInfo.parallelStream().filter(permissionInfo -> {
            String uri = permissionInfo.getUri();
            if (uri.indexOf("{") > 0) {
                uri = uri.replaceAll("\\{\\*\\}", "[a-zA-Z\\\\d]+");
            }
            String regEx = "^" + uri + "$";
            return (Pattern.compile(regEx).matcher(requestUri).find())
                    && method.equals(permissionInfo.getMethod());
        });
    }

    /**
     * 判断本次请求的数据类型是否为json
     *
     * @param request request
     * @return boolean
     */
    private boolean isJson(HttpServletRequest request) {
        if (request.getContentType() != null) {
            return request.getContentType().equals(MediaType.APPLICATION_JSON_VALUE) ||
                    request.getContentType().equals(MediaType.APPLICATION_JSON_UTF8_VALUE);
        }

        return false;
    }
}
