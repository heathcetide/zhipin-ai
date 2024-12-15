package com.zhi.pin.model.vo;

import com.zhi.pin.model.entity.Question;

public class DailyQuestionVO {
    private Question question;

    private String questionImg;

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public String getQuestionImg() {
        return questionImg;
    }

    public void setQuestionImg(String questionImg) {
        this.questionImg = questionImg;
    }
}
