package com.zhi.pin.model.entity;

public class ApiResponse {
    private String finish_reason;
    private int index;
    private Message message;
    private Object delta; // delta 可以为 null 或其他类型，根据实际情况选择 Object 或 JsonNode

    public String getFinish_reason() {
        return finish_reason;
    }

    public void setFinish_reason(String finish_reason) {
        this.finish_reason = finish_reason;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public Object getDelta() {
        return delta;
    }

    public void setDelta(Object delta) {
        this.delta = delta;
    }
}
