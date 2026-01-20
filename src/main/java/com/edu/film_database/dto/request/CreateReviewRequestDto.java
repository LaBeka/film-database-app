package com.edu.film_database.dto.request;

import jakarta.validation.constraints.NotNull;

import java.util.Objects;

public class CreateReviewRequestDto {

    @NotNull
    private int filmId;
    private int score;
    @NotNull
    private String text;

    public CreateReviewRequestDto(int filmId, int score, String text) {
        this.filmId = filmId;
        this.score = score;
        this.text = text;
    }

    public int getFilmId() {
        return filmId;
    }

    public void setFilmId(int filmId) {
        this.filmId = filmId;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "CreateReviewRequestDto{" +
                "filmId=" + filmId +
                ", score=" + score +
                ", text='" + text + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CreateReviewRequestDto that = (CreateReviewRequestDto) o;
        return filmId == that.filmId && score == that.score && Objects.equals(text, that.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(filmId, score, text);
    }
}
