package com.zhi.pin.model.dto.postfavour;

import java.io.Serializable;

/**
 * 帖子收藏 / 取消收藏请求
 *
 */
public class PostFavourAddRequest implements Serializable {

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