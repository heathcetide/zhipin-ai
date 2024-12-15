package com.zhi.pin.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @TableName interview_records
 */
@TableName(value ="interview_records")
public class InterviewRecords implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer record_id;

    /**
     * 
     */
    private Long interview_id;

    /**
     * 
     */
    private String question;

    /**
     * 
     */
    private String candidate_response;

    /**
     * 
     */
    private Integer ai_evaluation_score;

    /**
     * 
     */
    private String evaluation_comments;

    /**
     * 
     */
    private Date created_at;

    /**
     * 
     */
    private Date updated_at;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    public InterviewRecords(){

    }

    public InterviewRecords(String question, Long interview_id, String candidate_response) {
        this.question = question;
        this.interview_id = interview_id;
        this.candidate_response = candidate_response;
    }

    /**
     * 
     */
    public Integer getRecord_id() {
        return record_id;
    }

    /**
     * 
     */
    public void setRecord_id(Integer record_id) {
        this.record_id = record_id;
    }

    /**
     * 
     */
    public Long getInterview_id() {
        return interview_id;
    }

    /**
     * 
     */
    public void setInterview_id(Long interview_id) {
        this.interview_id = interview_id;
    }

    /**
     * 
     */
    public String getQuestion() {
        return question;
    }

    /**
     * 
     */
    public void setQuestion(String question) {
        this.question = question;
    }

    /**
     * 
     */
    public String getCandidate_response() {
        return candidate_response;
    }

    /**
     * 
     */
    public void setCandidate_response(String candidate_response) {
        this.candidate_response = candidate_response;
    }

    /**
     * 
     */
    public Integer getAi_evaluation_score() {
        return ai_evaluation_score;
    }

    /**
     * 
     */
    public void setAi_evaluation_score(Integer ai_evaluation_score) {
        this.ai_evaluation_score = ai_evaluation_score;
    }

    /**
     * 
     */
    public String getEvaluation_comments() {
        return evaluation_comments;
    }

    /**
     * 
     */
    public void setEvaluation_comments(String evaluation_comments) {
        this.evaluation_comments = evaluation_comments;
    }

    /**
     * 
     */
    public Date getCreated_at() {
        return created_at;
    }

    /**
     * 
     */
    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    /**
     * 
     */
    public Date getUpdated_at() {
        return updated_at;
    }

    /**
     * 
     */
    public void setUpdated_at(Date updated_at) {
        this.updated_at = updated_at;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        InterviewRecords other = (InterviewRecords) that;
        return (this.getRecord_id() == null ? other.getRecord_id() == null : this.getRecord_id().equals(other.getRecord_id()))
            && (this.getInterview_id() == null ? other.getInterview_id() == null : this.getInterview_id().equals(other.getInterview_id()))
            && (this.getQuestion() == null ? other.getQuestion() == null : this.getQuestion().equals(other.getQuestion()))
            && (this.getCandidate_response() == null ? other.getCandidate_response() == null : this.getCandidate_response().equals(other.getCandidate_response()))
            && (this.getAi_evaluation_score() == null ? other.getAi_evaluation_score() == null : this.getAi_evaluation_score().equals(other.getAi_evaluation_score()))
            && (this.getEvaluation_comments() == null ? other.getEvaluation_comments() == null : this.getEvaluation_comments().equals(other.getEvaluation_comments()))
            && (this.getCreated_at() == null ? other.getCreated_at() == null : this.getCreated_at().equals(other.getCreated_at()))
            && (this.getUpdated_at() == null ? other.getUpdated_at() == null : this.getUpdated_at().equals(other.getUpdated_at()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getRecord_id() == null) ? 0 : getRecord_id().hashCode());
        result = prime * result + ((getInterview_id() == null) ? 0 : getInterview_id().hashCode());
        result = prime * result + ((getQuestion() == null) ? 0 : getQuestion().hashCode());
        result = prime * result + ((getCandidate_response() == null) ? 0 : getCandidate_response().hashCode());
        result = prime * result + ((getAi_evaluation_score() == null) ? 0 : getAi_evaluation_score().hashCode());
        result = prime * result + ((getEvaluation_comments() == null) ? 0 : getEvaluation_comments().hashCode());
        result = prime * result + ((getCreated_at() == null) ? 0 : getCreated_at().hashCode());
        result = prime * result + ((getUpdated_at() == null) ? 0 : getUpdated_at().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", record_id=").append(record_id);
        sb.append(", interview_id=").append(interview_id);
        sb.append(", question=").append(question);
        sb.append(", candidate_response=").append(candidate_response);
        sb.append(", ai_evaluation_score=").append(ai_evaluation_score);
        sb.append(", evaluation_comments=").append(evaluation_comments);
        sb.append(", created_at=").append(created_at);
        sb.append(", updated_at=").append(updated_at);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}