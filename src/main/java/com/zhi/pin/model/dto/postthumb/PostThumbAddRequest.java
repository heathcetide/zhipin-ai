package com.zhi.pin.model.dto.postthumb;

import java.io.Serializable;

/**
 * 帖子点赞请求
 *
 */
public class PostThumbAddRequest implements Serializable {

    /**
     * 帖子 id
     */
    private Long postId;

    private static final long serialVersionUID = 1L;

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }
}