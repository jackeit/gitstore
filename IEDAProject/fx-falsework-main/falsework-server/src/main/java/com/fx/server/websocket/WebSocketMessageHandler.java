package com.fx.server.websocket;

import com.fx.server.constant.CommonConstants;
import com.fx.server.entity.log.SysLoginInfor;
import com.fx.server.exception.auth.UserInvalidException;
import com.fx.server.service.log.SysLoginInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author liwen
 * @Description: WebSocketMessageHandler
 * @param:
 * @return:
 * @auther: liwen
 * @date: 2021/2/4 9:27 下午
 */
@Slf4j
@Component
public class WebSocketMessageHandler extends TextWebSocketHandler {

    /**
     * 用来存储当前在线的客户端(此map线程安全)
     */
    private static ConcurrentHashMap<String, WebSocketSession> WEBSOCKET_MAP = new ConcurrentHashMap<>();

    /**
     * 静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
     */
    private static final AtomicInteger ONLINE_NUM = new AtomicInteger();
    @Autowired
    private SysLoginInfoService sysLoginInfoService;

    /**
     * 当和用户成功建立连接的时候会调用此方法,在此方法内部应该保存连接
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("建立连接成功");
        /* 将在拦截器中保存的用户的名字取出来,然后作为 key 存到 map 中 */
        String userId = (String) session.getAttributes().get(CommonConstants.WEB_SOCKET_USER_ID);
        SysLoginInfor sysLoginInfor = (SysLoginInfor) session.getAttributes().get(CommonConstants.WEB_SOCKET_USER_LIST);

        if (userId != null) {
            /* 保存当前的连接和用户之间的关系 */
            WEBSOCKET_MAP.put(userId, session);
            /* 在线数加1 */
            addOnlineCount();
            log.info("客户端" + sysLoginInfor.getLoginName() + "加入，当前在线数为：" + ONLINE_NUM.get());
            sendMessageToUser(userId, new TextMessage(userId + "WebSocket连接成功"));
        }
        /* 这块会实现自己业务，比如，当用户登录后，会把离线消息推送给用户 */

    }

    /**
     * 收到消息的时候会触发该方法
     *
     * @param session 发送消息的用户的 session
     * @param message 发送的内容
     * @throws Exception
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
//        //此处请根据自己的具体业务逻辑做处理
//        JSONObject jsonObject = JSONObject.fromObject(new String(message.asBytes()));//将用户发送的消息转换为 json,实际开发中请根据自己的需求处理
//        String toName = jsonObject.getString("toName");//获取数据中的收消息人的名字
//        String content = jsonObject.getString("content");//获取到发送的内容
//        String fromName = (String) session.getAttributes().get(Constants.WEBSOCKET_USERNAME);//获取当前发送消息的人的名字
//        content = "收到来自:" + fromName + "的消息,内容是:" + content;
//        //拼接内容转发给接收者,实际开发中请参考自己的需求做处理
//        TextMessage textMessage = new TextMessage(content);//将内容转换为 TextMessage
//        sendMessageToUser(toName, textMessage);// 发送给指定的用户
//        //sendMessageToUsers(message);//给所有人发送
//        //super.handleTextMessage(session, message);
    }

    /**
     * @Description: 给某个用户发送消息
     * @param: [userId, message]
     * @return: void
     * @auther: liwen
     * @date: 2021/2/4 9:27 下午
     */
    public void sendMessageToUser(String userId, TextMessage message) {
        /* 根据接收方的名字找到对应的连接 */
        WebSocketSession webSocketSession = WEBSOCKET_MAP.get(userId);
        //如果没有离线,如果离线,请根据实际业务需求来处理,可能会需要保存离线消息
        if (webSocketSession != null && webSocketSession.isOpen()) {
            try {
                /* 发送消息 */
                webSocketSession.sendMessage(message);
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    /**
     * @Description: 给所有在线用户发送消息, 此处以文本消息为例子
     * @param: [message]
     * @return: void
     * @auther: liwen
     * @date: 2021/2/4 9:29 下午
     */
    public void sendMessageToUsers(TextMessage message) {
        /* 获取所有的连接 */
        for (Map.Entry<String, WebSocketSession> webSocketSessionEntry : WEBSOCKET_MAP.entrySet()) {
            /* 找到每个连接 */
            WebSocketSession session = webSocketSessionEntry.getValue();
            if (session != null && session.isOpen()) {
                try {
                    session.sendMessage(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 出现异常的时候
     *
     * @param session
     * @param exception
     * @throws Exception
     */
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        String userId = (String) session.getAttributes().get(CommonConstants.WEB_SOCKET_USER_ID);
        if (session.isOpen()) {
            session.close();
        }

        log.error("WebSocket发生错误");
        /* 移除连接 */
        WEBSOCKET_MAP.remove(userId);
        /* 在线数减1 */
        subOnlineCount();

        SysLoginInfor sysLoginInfor = (SysLoginInfor) session.getAttributes().get(CommonConstants.WEB_SOCKET_USER_LIST);
        sysLoginInfor.setMsg("连接异常");
        sysLoginInfor.setStatus("1");
        sysLoginInfoService.addSysJobLog(sysLoginInfor);
    }

    /**
     * 连接关闭后
     *
     * @param session
     * @param closeStatus
     * @throws Exception
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        /* 找到用户对应的连接 */
        String userId = (String) session.getAttributes().get(CommonConstants.WEB_SOCKET_USER_ID);

        /* 从map中删除 */
        WEBSOCKET_MAP.remove(userId);
        /* 在线数减1 */
        subOnlineCount();
        SysLoginInfor sysLoginInfor = (SysLoginInfor) session.getAttributes().get(CommonConstants.WEB_SOCKET_USER_LIST);
        sysLoginInfor.setStatus("1");
        sysLoginInfor.setMsg("退出登录");
        sysLoginInfoService.addSysJobLog(sysLoginInfor);
        log.info("[" + sysLoginInfor.getLoginName() + "]连接关闭，当前在线数为：" + ONLINE_NUM.get());
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }


    /**
     * 有人上线时在线人数加一
     */
    private static synchronized void addOnlineCount() {
        ONLINE_NUM.incrementAndGet();
    }

    /**
     * 有人下线时在线人数减一
     */
    private static void subOnlineCount() {
        if (ONLINE_NUM.get() > 0) {
            ONLINE_NUM.decrementAndGet();

        }
    }


    public List<SysLoginInfor> getOnlineUsers() {
        List<SysLoginInfor> onlineUsers = new ArrayList<>();
        /* 获取所有的连接 */
        for (Map.Entry<String, WebSocketSession> webSocketSessionEntry : WEBSOCKET_MAP.entrySet()) {
            /* 找到每个连接 */
            WebSocketSession session = webSocketSessionEntry.getValue();
            if (session != null && session.isOpen()) {
                SysLoginInfor sysLoginInfor = (SysLoginInfor) session.getAttributes().get(CommonConstants.WEB_SOCKET_USER_LIST);
                onlineUsers.add(sysLoginInfor);
            }
        }
        return onlineUsers;
    }

    public boolean retreat(String userId) {
        /* 根据接收方的名字找到对应的连接 */
        WebSocketSession webSocketSession = WEBSOCKET_MAP.get(userId);
        try {
            webSocketSession.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            throw new UserInvalidException("退出失败");

        }
    }


}