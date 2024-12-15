package com.zhi.pin.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @TableName interview_trends
 */
@TableName(value ="interview_trends")
public class InterviewTrends implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer trend_id;

    /**
     * 
     */
    private Integer candidate_id;

    /**
     * 
     */
    private String trend_data;

    /**
     * 
     */
    private String trend_summary;

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
    public Integer getTrend_id() {
        return trend_id;
    }

    /**
     * 
     */
    public void setTrend_id(Integer trend_id) {
        this.trend_id = trend_id;
    }

    /**
     * 
     */
    public Integer getCandidate_id() {
        return candidate_id;
    }

    /**
     * 
     */
    public void setCandidate_id(Integer candidate_id) {
        this.candidate_id = candidate_id;
    }

    /**
     * 
     */
    public String getTrend_data() {
        return trend_data;
    }

    /**
     * 
     */
    public void setTrend_data(String trend_data) {
        this.trend_data = trend_data;
    }

    /**
     * 
     */
    public String getTrend_summary() {
        return trend_summary;
    }

    /**
     * 
     */
    public void setTrend_summary(String trend_summary) {
        this.trend_summary = trend_summary;
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
        InterviewTrends other = (InterviewTrends) that;
        return (this.getTrend_id() == null ? other.getTrend_id() == null : this.getTrend_id().equals(other.getTrend_id()))
            && (this.getCandidate_id() == null ? other.getCandidate_id() == null : this.getCandidate_id().equals(other.getCandidate_id()))
            && (this.getTrend_data() == null ? other.getTrend_data() == null : this.getTrend_data().equals(other.getTrend_data()))
            && (this.getTrend_summary() == null ? other.getTrend_summary() == null : this.getTrend_summary().equals(other.getTrend_summary()))
            && (this.getCreated_at() == null ? other.getCreated_at() == null : this.getCreated_at().equals(other.getCreated_at()))
            && (this.getUpdated_at() == null ? other.getUpdated_at() == null : this.getUpdated_at().equals(other.getUpdated_at()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getTrend_id() == null) ? 0 : getTrend_id().hashCode());
        result = prime * result + ((getCandidate_id() == null) ? 0 : getCandidate_id().hashCode());
        result = prime * result + ((getTrend_data() == null) ? 0 : getTrend_data().hashCode());
        result = prime * result + ((getTrend_summary() == null) ? 0 : getTrend_summary().hashCode());
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
        sb.append(", trend_id=").append(trend_id);
        sb.append(", candidate_id=").append(candidate_id);
        sb.append(", trend_data=").append(trend_data);
        sb.append(", trend_summary=").append(trend_summary);
        sb.append(", created_at=").append(created_at);
        sb.append(", updated_at=").append(updated_at);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}