package com.zhi.pin.common;

import java.io.Serializable;

/**
 * 删除请求
 *
 */
public class DeleteRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}