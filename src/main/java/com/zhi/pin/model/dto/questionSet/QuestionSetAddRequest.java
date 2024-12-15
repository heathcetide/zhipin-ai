package com.zhi.pin.model.dto.questionSet;

import com.baomidou.mybatisplus.annotation.TableField;

import java.util.List;

public class QuestionSetAddRequest {

    /**
     * 题目集名称
     */
    private String name;

    /**
     * 题目集描述
     */
    private String description;

    /**
     * 题目 id 列表 (json 数组)
     */
    private List<String> questionIds;

    /**
     * 创建用户 id
     */
    private Long userId;


    @TableField(exist = false)
    private static final long serialVersionUID = 1L;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getQuestionIds() {
        return questionIds;
    }

    public void setQuestionIds(List<String> questionIds) {
        this.questionIds = questionIds;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "QuestionSetAddRequest{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", questionIds=" + questionIds +
                ", userId=" + userId +
                '}';
    }
}
