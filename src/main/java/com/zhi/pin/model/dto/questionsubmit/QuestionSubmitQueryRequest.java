package com.zhi.pin.model.dto.questionsubmit;

import com.zhi.pin.common.PageRequest;

import java.io.Serializable;

/**
 * 查询请求
 *
 */
public class QuestionSubmitQueryRequest extends PageRequest implements Serializable {

    /**
     * 编程语言
     */
    private String language;

    /**
     * 提交状态
     */
    private Integer status;

    /**
     * 题目 id
     */
    private Long questionId;

    /**
     * 用户 id
     */
    private Long userId;

    // 默认构造器
    public QuestionSubmitQueryRequest() {
    }

    // 全参数构造器
    public QuestionSubmitQueryRequest(String language, Integer status, Long questionId, Long userId) {
        this.language = language;
        this.status = status;
        this.questionId = questionId;
        this.userId = userId;
    }

    // Getter and Setter

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    // Override equals and hashCode methods
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof QuestionSubmitQueryRequest)) return false;
        if (!super.equals(o)) return false;

        QuestionSubmitQueryRequest that = (QuestionSubmitQueryRequest) o;

        if (language != null ? !language.equals(that.language) : that.language != null) return false;
        if (status != null ? !status.equals(that.status) : that.status != null) return false;
        if (questionId != null ? !questionId.equals(that.questionId) : that.questionId != null) return false;
        return userId != null ? userId.equals(that.userId) : that.userId == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (language != null ? language.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (questionId != null ? questionId.hashCode() : 0);
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        return result;
    }

    private static final long serialVersionUID = 1L;
}
