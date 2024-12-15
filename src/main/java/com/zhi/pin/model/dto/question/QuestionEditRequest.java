package com.zhi.pin.model.dto.question;

import java.io.Serializable;
import java.util.List;

/**
 * 编辑请求
 *
 */
public class QuestionEditRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 标签列表
     */
    private List<String> tags;

    /**
     * 题目答案
     */
    private String answer;

    /**
     * 判题用例
     */
    private List<JudgeCase> judgeCase;

    /**
     * 判题配置
     */
    private JudgeConfig judgeConfig;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public List<JudgeCase> getJudgeCase() {
        return judgeCase;
    }

    public void setJudgeCase(List<JudgeCase> judgeCase) {
        this.judgeCase = judgeCase;
    }

    public JudgeConfig getJudgeConfig() {
        return judgeConfig;
    }

    public void setJudgeConfig(JudgeConfig judgeConfig) {
        this.judgeConfig = judgeConfig;
    }
}