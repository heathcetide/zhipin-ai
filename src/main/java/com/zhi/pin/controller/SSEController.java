package com.zhi.pin.controller;

import com.zhi.pin.common.BaseResponse;
import com.zhi.pin.common.ResultUtils;
import com.zhi.pin.model.entity.MessageQueueService;
import com.zhi.pin.model.entity.RedisPublisher;
import com.zhi.pin.service.LeaderboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;

import javax.annotation.Resource;
import java.io.IOException;
import java.time.Duration;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@RestController
public class SSEController {

    @Autowired
    private RedisPublisher redisPublisher;

    @PostMapping("/publish")
    public String publishMessage(@RequestParam String userId, @RequestParam String message) {
        // 发布消息到用户的专属频道
        String channel = "channel:user:" + userId;
        redisPublisher.publish(channel, message);
        return "Message sent to user " + userId + ": " + message;
    }

    @Resource
    private LeaderboardService leaderboardService;

    // 每隔5秒推送一次数据
    @GetMapping(value = "/sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<BaseResponse<Set<String>>> streamData() {
        return Flux.interval(Duration.ofSeconds(5))
                .map(sequence -> {
                    Set<String> topN = leaderboardService.getTopN(10);
                    System.out.println("数据来了"+topN);
                    return ResultUtils.success(topN);
                });
    }

    @Autowired
    private MessageQueueService messageQueueService;

    // 保存用户 ID 和对应的 SSE 连接
    private final ConcurrentHashMap<String, SseEmitter> userEmitters = new ConcurrentHashMap<>();

    /**
     * 用户订阅接口
     * @param userId 用户 ID
     * @return SSE Emitter
     */
    @GetMapping(value = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(@RequestParam String userId) {
        SseEmitter emitter = new SseEmitter();

        // 将用户 ID 和 SSE 映射保存
        userEmitters.put(userId, emitter);

        // 设置 SSE 生命周期的回调，清理无效连接
        emitter.onCompletion(() -> userEmitters.remove(userId));
        emitter.onTimeout(() -> userEmitters.remove(userId));
        emitter.onError(e -> userEmitters.remove(userId));

        return emitter;
    }

    /**
     * 推送消息到指定用户
     * @param userId 用户 ID
     * @param message 消息内容
     */
    public void sendToUser(String userId, String message) {
        SseEmitter emitter = userEmitters.get(userId);
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event().name("message").data(message));
            } catch (IOException e) {
                // 推送失败时，清理连接
                emitter.complete();
                userEmitters.remove(userId);
            }
        } else {
            System.out.println("User " + userId + " is not connected.");
        }
    }

}