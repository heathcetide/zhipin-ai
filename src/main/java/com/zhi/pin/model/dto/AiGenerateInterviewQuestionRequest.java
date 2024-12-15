package com.zhi.pin.model.dto;

import lombok.Data;

/**
 * 表示生成面试题目请求的实体类
 */
@Data
public class AiGenerateInterviewQuestionRequest {
    private Integer recordId;
    private Long interviewId;
    private Long positionId;
    private String positionName;
    private String positionDesc;
    private String positionCategory;
    private int questionNumber;
    private int optionNumber;
}
