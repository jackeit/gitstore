package com.fx.server.config;

import com.fx.server.interceptor.WebcocketInterceptor;
import com.fx.server.websocket.WebSocketMessageHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Autowired
    private WebSocketMessageHandler webSocketMessageHandler;
    @Autowired
    private WebcocketInterceptor myInterceptor;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry
                .addHandler(webSocketMessageHandler, "/websocket/*")
                .addInterceptors(myInterceptor)
                .setAllowedOrigins("*");
    }
}