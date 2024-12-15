package com.zhi.pin.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @TableName positions
 */
@TableName(value ="positions")
public class Positions implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long position_id;

    /**
     * 
     */
    private String position_name;

    /**
     * 
     */
    private String position_desc;

    /**
     * 
     */
    private Integer position_type;

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
    public Long getPosition_id() {
        return position_id;
    }

    /**
     * 
     */
    public void setPosition_id(Long position_id) {
        this.position_id = position_id;
    }

    /**
     * 
     */
    public String getPosition_name() {
        return position_name;
    }

    /**
     * 
     */
    public void setPosition_name(String position_name) {
        this.position_name = position_name;
    }

    /**
     * 
     */
    public String getPosition_desc() {
        return position_desc;
    }

    /**
     * 
     */
    public void setPosition_desc(String position_desc) {
        this.position_desc = position_desc;
    }

    /**
     * 
     */
    public Integer getPosition_type() {
        return position_type;
    }

    /**
     * 
     */
    public void setPosition_type(Integer position_type) {
        this.position_type = position_type;
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
        Positions other = (Positions) that;
        return (this.getPosition_id() == null ? other.getPosition_id() == null : this.getPosition_id().equals(other.getPosition_id()))
            && (this.getPosition_name() == null ? other.getPosition_name() == null : this.getPosition_name().equals(other.getPosition_name()))
            && (this.getPosition_desc() == null ? other.getPosition_desc() == null : this.getPosition_desc().equals(other.getPosition_desc()))
            && (this.getPosition_type() == null ? other.getPosition_type() == null : this.getPosition_type().equals(other.getPosition_type()))
            && (this.getCreated_at() == null ? other.getCreated_at() == null : this.getCreated_at().equals(other.getCreated_at()))
            && (this.getUpdated_at() == null ? other.getUpdated_at() == null : this.getUpdated_at().equals(other.getUpdated_at()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getPosition_id() == null) ? 0 : getPosition_id().hashCode());
        result = prime * result + ((getPosition_name() == null) ? 0 : getPosition_name().hashCode());
        result = prime * result + ((getPosition_desc() == null) ? 0 : getPosition_desc().hashCode());
        result = prime * result + ((getPosition_type() == null) ? 0 : getPosition_type().hashCode());
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
        sb.append(", position_id=").append(position_id);
        sb.append(", position_name=").append(position_name);
        sb.append(", position_desc=").append(position_desc);
        sb.append(", position_type=").append(position_type);
        sb.append(", created_at=").append(created_at);
        sb.append(", updated_at=").append(updated_at);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}