package com.zhi.pin.service;

import java.util.Set;

public interface LeaderboardService {
    // 添加或更新用户分数
    void addScore(String user, double score);

    // 增加用户分数
    void incrementScore(String user, double increment);
    // 获取前N名
    Set<String> getTopN(int n);

    // 获取用户排名
    Long getUserRank(String user);

    // 获取用户分数
    Double getUserScore(String user);
}
