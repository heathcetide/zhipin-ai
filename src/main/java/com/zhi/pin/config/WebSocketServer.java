package com.zhi.pin.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ServerEndpoint("/ws/{sid}")
public class WebSocketServer {

    // 存放会话对象
    private static final Map<String, Session> sessionMap = new ConcurrentHashMap<>();

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("sid") String sid) {
        System.out.println("客户端：" + sid + " 建立连接");
        sessionMap.put(sid, session);
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, @PathParam("sid") String sid) {
        System.out.println("收到来自客户端：" + sid + " 的消息：" + message);

        try {
            // 解析消息
            MessageData messageData = MessageData.fromJson(message);

            if (messageData.getTo() != null) {
                // 单聊
                sendToClient(messageData.getTo(), messageData.getContent());
            } else {
                // 群发
                sendToAllClients(messageData.getContent());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(@PathParam("sid") String sid) {
        System.out.println("连接断开: " + sid);
        sessionMap.remove(sid);
    }

    /**
     * 单聊
     *
     * @param sid     目标用户的唯一标识
     * @param message 消息内容
     */
    private void sendToClient(String sid, String message) {
        Session session = sessionMap.get(sid);
        if (session != null && session.isOpen()) {
            try {
                session.getBasicRemote().sendText(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("用户 " + sid + " 不在线");
        }
    }

    /**
     * 群发
     *
     * @param message 消息内容
     */
    private void sendToAllClients(String message) {
        Collection<Session> sessions = sessionMap.values();
        for (Session session : sessions) {
            try {
                session.getBasicRemote().sendText(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 消息数据结构
     */
     static class MessageData {
        private String to; // 目标用户
        private String content; // 消息内容

        // JSON 解析方法（可使用 Jackson 或 Gson 实现）
        public static MessageData fromJson(String json) throws JsonProcessingException {
            // 假设使用 Jackson
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(json, MessageData.class);
        }

        // Getters and Setters
        public String getTo() {
            return to;
        }

        public void setTo(String to) {
            this.to = to;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
