package com.edu.film_database.dto.request;

import java.util.Objects;

public class UpdateReviewRequestDto {

    private int reviewIndex;
    private int score;
    private String text;

    public UpdateReviewRequestDto(int reviewIndex, int score, String text) {
        this.reviewIndex = reviewIndex;
        this.score = score;
        this.text = text;
    }

    public int getReviewIndex() {
        return reviewIndex;
    }

    public int getScore() {
        return score;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return "UpdateReviewRequestDto{" +
                "reviewIndex=" + reviewIndex +
                ", score=" + score +
                ", text='" + text + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        UpdateReviewRequestDto that = (UpdateReviewRequestDto) o;
        return reviewIndex == that.reviewIndex && score == that.score && Objects.equals(text, that.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reviewIndex, score, text);
    }
}
