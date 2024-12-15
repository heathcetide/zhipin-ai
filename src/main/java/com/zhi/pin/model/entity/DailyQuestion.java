package com.zhi.pin.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

/**
 * 每日题目记录
 * @TableName daily_question
 */
@TableName(value ="daily_question")
public class DailyQuestion implements Serializable {
    /**
     * 记录 id
     */
    @TableId
    private Long id;

    /**
     * 随机选中的题目 id
     */
    private Long question_id;

    private String question_img;

    /**
     * 题目对应的日期
     */
    private Date date;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * 记录 id
     */
    public Long getId() {
        return id;
    }

    /**
     * 记录 id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 随机选中的题目 id
     */
    public Long getQuestion_id() {
        return question_id;
    }

    /**
     * 随机选中的题目 id
     */
    public void setQuestion_id(Long question_id) {
        this.question_id = question_id;
    }

    /**
     * 题目对应的日期
     */
    public Date getDate() {
        return date;
    }

    /**
     * 题目对应的日期
     */
    public void setDate(Date date) {
        this.date = date;
    }

    public String getQuestion_img() {
        return question_img;
    }

    public void setQuestion_img(String question_img) {
        this.question_img = question_img;
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
        DailyQuestion other = (DailyQuestion) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getQuestion_id() == null ? other.getQuestion_id() == null : this.getQuestion_id().equals(other.getQuestion_id()))
            && (this.getDate() == null ? other.getDate() == null : this.getDate().equals(other.getDate()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getQuestion_id() == null) ? 0 : getQuestion_id().hashCode());
        result = prime * result + ((getDate() == null) ? 0 : getDate().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", question_id=").append(question_id);
        sb.append(", date=").append(date);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}