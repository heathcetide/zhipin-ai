package com.zhi.pin.model.entity;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.springframework.stereotype.Service;
@Service
public class MessageQueueService {
    // 按频道存储消息队列
    private final Map<String, ConcurrentLinkedQueue<String>> channelMessageQueues = new ConcurrentHashMap<>();

    // 添加消息到特定频道的队列
    public void addMessage(String channel, String message) {
        System.out.println("Adding message to channel: " + channel + ", message: " + message);
        channelMessageQueues
                .computeIfAbsent(channel, key -> new ConcurrentLinkedQueue<>())
                .add(message);
    }

    // 从特定频道获取下一条消息
    public String getNextMessage(String channel) {
        ConcurrentLinkedQueue<String> queue = channelMessageQueues.get(channel);
        if (queue != null) {
            String message = queue.poll();
            System.out.println("Polling message from channel: " + channel + ", message: " + message);
            return message;
        }
        return null;
    }
}
