package com.zhi.pin.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @TableName interview_feedback
 */
@TableName(value ="interview_feedback")
public class InterviewFeedback implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer feedback_id;

    /**
     * 
     */
    private Long interview_id;

    /**
     * 
     */
    private Integer record_id;

    /**
     * 
     */
    private String strengths;

    /**
     * 
     */
    private String weaknesses;

    /**
     * 
     */
    private String recommendations;

    /**
     * 
     */
    private String follow_up_actions;

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

    /**
     * 
     */
    public Integer getFeedback_id() {
        return feedback_id;
    }

    /**
     * 
     */
    public void setFeedback_id(Integer feedback_id) {
        this.feedback_id = feedback_id;
    }

    public Long getInterview_id() {
        return interview_id;
    }

    public void setInterview_id(Long interview_id) {
        this.interview_id = interview_id;
    }

    public Integer getRecord_id() {
        return record_id;
    }

    public void setRecord_id(Integer record_id) {
        this.record_id = record_id;
    }

    /**
     * 
     */
    public String getStrengths() {
        return strengths;
    }

    /**
     * 
     */
    public void setStrengths(String strengths) {
        this.strengths = strengths;
    }

    /**
     * 
     */
    public String getWeaknesses() {
        return weaknesses;
    }

    /**
     * 
     */
    public void setWeaknesses(String weaknesses) {
        this.weaknesses = weaknesses;
    }

    /**
     * 
     */
    public String getRecommendations() {
        return recommendations;
    }

    /**
     * 
     */
    public void setRecommendations(String recommendations) {
        this.recommendations = recommendations;
    }

    /**
     * 
     */
    public String getFollow_up_actions() {
        return follow_up_actions;
    }

    /**
     * 
     */
    public void setFollow_up_actions(String follow_up_actions) {
        this.follow_up_actions = follow_up_actions;
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
    public String toString() {
        return "InterviewFeedback{" +
                "feedback_id=" + feedback_id +
                ", interview_id=" + interview_id +
                ", record_id=" + record_id +
                ", strengths='" + strengths + '\'' +
                ", weaknesses='" + weaknesses + '\'' +
                ", recommendations='" + recommendations + '\'' +
                ", follow_up_actions='" + follow_up_actions + '\'' +
                ", created_at=" + created_at +
                ", updated_at=" + updated_at +
                '}';
    }
}