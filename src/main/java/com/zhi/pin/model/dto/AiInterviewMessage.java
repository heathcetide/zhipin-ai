package com.zhi.pin.model.dto;

public class AiInterviewMessage {
    private String userMessage;
    private Integer record_id;
    private Long interview_id;

    public String getUserMessage() {
        return userMessage;
    }

    public void setUserMessage(String userMessage) {
        this.userMessage = userMessage;
    }

    public Integer getRecord_id() {
        return record_id;
    }

    public void setRecord_id(Integer record_id) {
        this.record_id = record_id;
    }

    public Long getInterview_id() {
        return interview_id;
    }

    public void setInterview_id(Long interview_id) {
        this.interview_id = interview_id;
    }
}
