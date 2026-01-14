package com.edu.film_database.dto.request;

import java.util.Objects;

public class UpdateReviewRequestDto {

    private int reviewIndex;
    private String filmTitle;
    private int score;
    private String text;

    public UpdateReviewRequestDto(int reviewIndex, String filmTitle, int score, String text) {
        this.reviewIndex = reviewIndex;
        this.filmTitle = filmTitle;
        this.score = score;
        this.text = text;
    }

    public int getReviewIndex() {
        return reviewIndex;
    }

    public String getFilmTitle() {
        return filmTitle;
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
                ", filmTitle='" + filmTitle + '\'' +
                ", score=" + score +
                ", text='" + text + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        UpdateReviewRequestDto that = (UpdateReviewRequestDto) o;
        return reviewIndex == that.reviewIndex && score == that.score && Objects.equals(filmTitle, that.filmTitle) && Objects.equals(text, that.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reviewIndex, filmTitle, score, text);
    }
}
