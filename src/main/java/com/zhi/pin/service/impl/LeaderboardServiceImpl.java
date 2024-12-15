package com.zhi.pin.service.impl;

import com.zhi.pin.service.LeaderboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class LeaderboardServiceImpl implements LeaderboardService {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private static final String LEADERBOARD_KEY = "leaderboard";


    // 添加或更新用户分数
    @Override
    public void addScore(String user, double score) {
        redisTemplate.opsForZSet().add(LEADERBOARD_KEY, user, score);
    }


    // 增加用户分数
    @Override
    public void incrementScore(String user, double increment) {
        redisTemplate.opsForZSet().incrementScore(LEADERBOARD_KEY, user, increment);
    }


    // 获取前N名
    @Override
    public Set<String> getTopN(int n) {
        return redisTemplate.opsForZSet().reverseRange(LEADERBOARD_KEY, 0, n - 1);
    }


    // 获取用户排名
    @Override
    public Long getUserRank(String user) {
        return redisTemplate.opsForZSet().reverseRank(LEADERBOARD_KEY, user);
    }

    // 获取用户分数
    @Override
    public Double getUserScore(String user) {
        return redisTemplate.opsForZSet().score(LEADERBOARD_KEY, user);
    }
}
