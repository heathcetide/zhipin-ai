package com.zhi.pin.model.dto;

import java.util.List;

/**
 * 用于表示面试题目内容的实体类
 */
public class QuestionContentDTO {

    /**
     * 题目标题
     */
    private String title;

    /**
     * 题目选项列表
     */
    private List<Option> options;

    // 默认构造器
    public QuestionContentDTO() {
    }

    // 全参数构造器
    public QuestionContentDTO(String title, List<Option> options) {
        this.title = title;
        this.options = options;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Option> getOptions() {
        return options;
    }

    public void setOptions(List<Option> options) {
        this.options = options;
    }

    /**
     * 题目选项
     */
    public static class Option {
        private String result;
        private int score;
        private String value;
        private String key;

        // 默认构造器
        public Option() {
        }

        // 全参数构造器
        public Option(String result, int score, String value, String key) {
            this.result = result;
            this.score = score;
            this.value = value;
            this.key = key;
        }

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }

        public int getScore() {
            return score;
        }

        public void setScore(int score) {
            this.score = score;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }
    }

    @Override
    public String toString() {
        return "QuestionContentDTO{" +
                "title='" + title + '\'' +
                ", options=" + options +
                '}';
    }
}
