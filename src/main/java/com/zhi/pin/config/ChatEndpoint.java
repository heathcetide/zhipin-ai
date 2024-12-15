package com.zhi.pin.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

@Component
@ServerEndpoint("/chat")
public class ChatEndpoint {

    @OnOpen
    public void onOpen(Session session, EndpointConfig config) {
        System.out.println("WebSocket连接已打开");
    }
    @OnMessage
    public void onMessage(String message) {
        System.out.println("收到消息：" + message);
    }

    @OnClose
    public void onClose(Session session) {
        System.out.println("WebSocket连接已关闭");
    }

}