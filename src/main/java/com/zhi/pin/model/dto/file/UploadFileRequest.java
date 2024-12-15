package com.zhi.pin.model.dto.file;

import java.io.Serializable;

/**
 * 文件上传请求
 *
 */
public class UploadFileRequest implements Serializable {

    /**
     * 业务
     */
    private String biz;


    private static final long serialVersionUID = 1L;

    public String getBiz() {
        return biz;
    }

    public void setBiz(String biz) {
        this.biz = biz;
    }
}