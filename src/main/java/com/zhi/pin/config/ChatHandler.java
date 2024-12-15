package com.zhi.pin.config;

import com.zhi.pin.model.entity.SessionBean;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *  websocket处理程序
 */
@Component
public class ChatHandler extends AbstractWebSocketHandler {
    private final Map<String, SessionBean> onlineUsers = new ConcurrentHashMap<>();
    private static AtomicInteger clientIdMaker;
    static {
        clientIdMaker = new AtomicInteger(0);
    }
    // 连接建立后
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
        SessionBean sessionBean = new SessionBean(session,clientIdMaker.getAndIncrement());
        onlineUsers.put(session.getId(), sessionBean);
        System.out.println(onlineUsers.get(session.getId()).getClientId()+ "建立了连接");
    }
    // 接收到消息
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        super.handleTextMessage(session, message);
        System.out.println("接收到消息："+message.getPayload());
        System.out.println(onlineUsers.get(session.getId()).getClientId() + " " + onlineUsers.get(session.getId()).getWebSocketSession());
    }
    // 异常
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        super.handleTransportError(session, exception);
        if (session.isOpen()){
            session.close();
        }
        onlineUsers.remove(session.getId());
    }
    // 关闭连接后
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
        System.out.println("连接关闭"+onlineUsers.get(session.getId()).getClientId());
    }

    @Scheduled(fixedDelay = 5000)
    public void sendMessage() throws IOException {
        for (String key : onlineUsers.keySet()){
            onlineUsers.get(key).getWebSocketSession().sendMessage(new TextMessage("hello"));
        }
    }
}
