package com.zhi.pin.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import javax.annotation.Resource;

@Configuration
@EnableWebSocket
public class ChatConfig implements WebSocketConfigurer {

    @Resource
    private ChatHandler chatHandler;

    @Resource
    private ChatInterceptor chatInterceptor;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry webSocketHandlerRegistry) {
        webSocketHandlerRegistry.addHandler(chatHandler, "/chat").addInterceptors(chatInterceptor)
                .setAllowedOrigins("*");
    }
}
