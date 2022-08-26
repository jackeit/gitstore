package com.fx.client.websocket;

import com.fx.client.gui.uicomponents.login.LoginController;
import com.fx.client.store.ApplicatonStore;
import io.datafx.controller.context.ApplicationContext;
import io.datafx.controller.flow.FlowException;
import io.datafx.controller.flow.FlowHandler;
import io.datafx.controller.injection.scopes.ApplicationScoped;
import io.datafx.controller.util.VetoException;
import io.datafx.core.concurrent.ProcessChain;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @className: Session
 * @author: liwen
 * @date: 2020/6/24 16:17
 */
@ApplicationScoped
@Slf4j
public class WSClient extends WebSocketClient {

    private static WSClient instance;


    public static synchronized WSClient getInstance() {
        if (instance == null) {
            Map<String, String> httpHeaders = new HashMap<>();
            try {
                instance = new WSClient(new URI("ws://localhost:8080/websocket/test"));
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }

        return instance;
    }


    public WSClient(URI serverUri) {
        super(serverUri);
    }


    @Override
    public void onOpen(ServerHandshake handshakedata) {
        log.info("[websocket] 连接成功");
        System.err.println(instance.isOpen());
    }

    @Override
    public void onMessage(String message) {
        log.info("[websocket] 收到消息={}", message);

    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        log.info("[websocket] 退出连接");
        instance = null;

        ProcessChain.create().addRunnableInPlatformThread(() -> {
            ApplicatonStore.clearPermissionInfo();
            FlowHandler flowHandler= (FlowHandler) ApplicationContext.getInstance().getRegisteredObject("ContentFlowHandler");
            try {
                flowHandler.navigateTo(LoginController.class);
            } catch (VetoException e) {
                e.printStackTrace();
            } catch (FlowException e) {
                e.printStackTrace();
            }
        }).run();


    }

    @Override
    public void onError(Exception ex) {
        log.info("[websocket] 连接错误={}", ex.getMessage());
        instance = null;
    }


}
