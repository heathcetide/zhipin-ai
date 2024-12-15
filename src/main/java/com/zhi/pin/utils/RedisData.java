package com.zhi.pin.utils;

import java.time.LocalDateTime;

public class RedisData {
    private LocalDateTime expireTime;

    private Object data;

    public LocalDateTime getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(LocalDateTime expireTime) {
        this.expireTime = expireTime;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
