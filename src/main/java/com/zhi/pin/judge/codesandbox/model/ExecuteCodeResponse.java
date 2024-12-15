package com.zhi.pin.judge.codesandbox.model;

import java.util.List;

/**
 * 执行代码响应
 */
public class ExecuteCodeResponse {

    private List<String> outputList;

    /**
     * 接口信息
     */
    private String message;

    /**
     * 执行状态
     */
    private Integer status;

    /**
     * 判题信息
     */
    private JudgeInfo judgeInfo;

    public ExecuteCodeResponse() {
    }

    public ExecuteCodeResponse(List<String> outputList, String message, Integer status, JudgeInfo judgeInfo) {
        this.outputList = outputList;
        this.message = message;
        this.status = status;
        this.judgeInfo = judgeInfo;
    }

    public List<String> getOutputList() {
        return outputList;
    }

    public void setOutputList(List<String> outputList) {
        this.outputList = outputList;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public JudgeInfo getJudgeInfo() {
        return judgeInfo;
    }

    public void setJudgeInfo(JudgeInfo judgeInfo) {
        this.judgeInfo = judgeInfo;
    }
}
