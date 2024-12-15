package com.zhi.pin.model.vo;

import com.zhi.pin.model.entity.QuestionSet;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;

public class QuestionSetVO implements Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * 标题
     */
    private String name;

    /**
     * 内容
     */
    private String description;

    /**
     * 创建用户 id
     */
    private Long userId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;


    private String questionIds;

    /**
     * 创建题目人的信息
     */
    private UserVO userVO;

    /**
     * 对象转包装类
     *
     * @param question
     * @return
     */
    public static QuestionSetVO objToVo(QuestionSet question) {
        if (question == null) {
            return null;
        }
        QuestionSetVO questionVO = new QuestionSetVO();
        BeanUtils.copyProperties(question, questionVO);
        return questionVO;
    }

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public UserVO getUserVO() {
        return userVO;
    }

    public void setUserVO(UserVO userVO) {
        this.userVO = userVO;
    }

    public String getQuestionIds() {
        return questionIds;
    }

    public void setQuestionIds(String questionIds) {
        this.questionIds = questionIds;
    }

    @Override
    public String toString() {
        return "QuestionSetVO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", userId=" + userId +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", questionIds=" + questionIds +
                ", userVO=" + userVO +
                '}';
    }
}
