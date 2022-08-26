package com.fx.server.handler;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import java.util.Map;

/**
 * WebSocket握手请求的拦截器. 检查握手请求和响应, 对WebSocketHandler传递属性
 */
public class ChatHandshakeInterceptor extends HttpSessionHandshakeInterceptor {
    /**
     * 在握手之前执行该方法, 继续握手返回true, 中断握手返回false. 通过attributes参数设置WebSocketSession的属性
     *
     * @param request
     * @param response
     * @param wsHandler
     * @param attributes
     * @return
     * @throws Exception
     */
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
                                   Map<String, Object> attributes) throws Exception {
//为了方便区分来源,在此以用户的名字来区分,名字我们通过要求用输入进行传递,所以在这里先从请求中获取到用户输入的名字,因为是使用的rest 风格,所以规定路径的最后一个字符串是名字
        System.out.println("握手之前");
        String s = request.getURI().toString();
        String s1 = s.substring(s.lastIndexOf("/") + 1);

        return super.beforeHandshake(request, response, wsHandler, attributes);
    }

    /**
     * 在握手之后执行该方法. 无论是否握手成功都指明了响应状态码和相应头.
     *
     * @param request
     * @param response
     * @param wsHandler
     * @param ex
     */
    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
                               Exception ex) {
        System.out.println("After Handshake");
        super.afterHandshake(request, response, wsHandler, ex);
    }


}