package com.zhi.pin.judge.codesandbox.model;

import java.util.List;

/**
 * 执行代码请求
 */
public class ExecuteCodeRequest {

    private List<String> inputList;

    private String code;

    private String language;

    public ExecuteCodeRequest() {
    }

    public ExecuteCodeRequest(List<String> inputList, String code, String language) {
        this.inputList = inputList;
        this.code = code;
        this.language = language;
    }

    public List<String> getInputList() {
        return inputList;
    }

    public void setInputList(List<String> inputList) {
        this.inputList = inputList;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
