package com.zhi.pin.model.entity;

import com.zhi.pin.controller.SSEController;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

@Service
public class RedisSubscriber implements MessageListener {

    @Autowired
    private MessageQueueService messageQueueService;

    @Autowired
    private SSEController sseController;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        // 从 Redis 消息中提取频道和消息内容
        String channel = new String(pattern);
        String body = new String(message.getBody());
        System.out.println("Received message: " + body + " from channel: " + channel);
        // 假设频道名格式为 "channel:user:{userId}"
        if (channel.startsWith("channel:user:")) {
            String userId = channel.replace("channel:user:", "");
            // 将消息推送给对应的用户
            sseController.sendToUser(userId, body);
            return;
        }
        // 将消息存入特定频道的队列
        messageQueueService.addMessage(channel, body);
    }
}
