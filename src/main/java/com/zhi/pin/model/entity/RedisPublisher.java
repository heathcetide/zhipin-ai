package com.zhi.pin.model.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

@Service
public class RedisPublisher {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public void publish(String channel, String message) {

        redisTemplate.execute(new SessionCallback<>() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                operations.multi(); // 开启事务
                // 发布消息
                operations.convertAndSend(channel, message);
                // 记录日志
                operations.opsForList().leftPush("log:" + channel, "Published: " + message);
                return operations.exec(); // 提交事务
            }
        });


//        redisTemplate.convertAndSend(channel, message);
        System.out.println("Published message: " + message + " to channel: " + channel);
    }
}
