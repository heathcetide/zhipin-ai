package com.zhi.pin.model.entity;

public class SelfInterview {
    private Integer score;
    private String description;

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "SelfInterview{" +
                "score=" + score +
                ", description='" + description + '\'' +
                '}';
    }
}
