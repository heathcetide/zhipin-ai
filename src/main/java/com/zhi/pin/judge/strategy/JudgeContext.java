package com.zhi.pin.judge.strategy;

import com.zhi.pin.judge.codesandbox.model.JudgeInfo;
import com.zhi.pin.model.dto.question.JudgeCase;
import com.zhi.pin.model.entity.Question;
import com.zhi.pin.model.entity.QuestionSubmit;

import java.util.List;

/**
 * 上下文（用于定义在策略中传递的参数）
 */
public class JudgeContext {

    private JudgeInfo judgeInfo;

    private List<String> inputList;

    private List<String> outputList;

    private List<JudgeCase> judgeCaseList;

    private Question question;

    private QuestionSubmit questionSubmit;

    public JudgeInfo getJudgeInfo() {
        return judgeInfo;
    }

    public void setJudgeInfo(JudgeInfo judgeInfo) {
        this.judgeInfo = judgeInfo;
    }

    public List<String> getInputList() {
        return inputList;
    }

    public void setInputList(List<String> inputList) {
        this.inputList = inputList;
    }

    public List<String> getOutputList() {
        return outputList;
    }

    public void setOutputList(List<String> outputList) {
        this.outputList = outputList;
    }

    public List<JudgeCase> getJudgeCaseList() {
        return judgeCaseList;
    }

    public void setJudgeCaseList(List<JudgeCase> judgeCaseList) {
        this.judgeCaseList = judgeCaseList;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public QuestionSubmit getQuestionSubmit() {
        return questionSubmit;
    }

    public void setQuestionSubmit(QuestionSubmit questionSubmit) {
        this.questionSubmit = questionSubmit;
    }
}
