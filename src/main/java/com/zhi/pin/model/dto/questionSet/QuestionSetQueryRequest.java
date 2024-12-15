package com.zhi.pin.model.dto.questionSet;

import com.zhi.pin.common.PageRequest;

import java.io.Serializable;
import java.util.List;

public class QuestionSetQueryRequest extends PageRequest implements Serializable {
    /**
     * id
     */
    private Long id;

    private String name;
    private String description;
    /**
     * 标签列表
     */
    private List<String> tags;

    /**
     * 创建用户 id
     */
    private Long userId;

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

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
