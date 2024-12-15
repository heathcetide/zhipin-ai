package com.zhi.pin.config;

import com.zhi.pin.model.entity.RedisSubscriber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 为 Redis 配置一个连接工厂以及消息监听容器。
 */
@Configuration
public class RedisConfig {


    @Autowired
    private RedisMessageListenerContainer redisContainer;

    // 存储已订阅的频道
    private final ConcurrentMap<String, Boolean> subscribedChannels = new ConcurrentHashMap<>();

    @Autowired
    private RedisSubscriber redisSubscriber;

    public void addSubscription(String channel) {
        ChannelTopic topic = new ChannelTopic(channel);
        MessageListenerAdapter listenerAdapter = new MessageListenerAdapter(redisSubscriber, "onMessage");
        redisContainer.addMessageListener(listenerAdapter, topic);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());
        return template;
    }

    @Bean
    public RedisMessageListenerContainer redisContainer(RedisConnectionFactory connectionFactory) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        return container;
    }

    public void addSubscription(String channel, RedisMessageListenerContainer redisContainer) {
        if (!isAlreadySubscribed(channel)) {
            // 添加到已订阅的频道列表
            subscribedChannels.put(channel, true);
            // 创建并注册监听器
            MessageListenerAdapter listenerAdapter = new MessageListenerAdapter(redisSubscriber, "onMessage");
            // 添加订阅
            redisContainer.addMessageListener(listenerAdapter, new ChannelTopic(channel));
            System.out.println("Subscribed to channel: " + channel);
        } else {
            System.out.println("Already subscribed to channel: " + channel);
        }
    }

    private boolean isAlreadySubscribed(String channel) {
        return subscribedChannels.containsKey(channel); // 检查频道是否已存在
    }
}
