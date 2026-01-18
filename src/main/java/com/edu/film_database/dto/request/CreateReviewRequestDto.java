package com.edu.film_database.dto.request;

import jakarta.validation.constraints.NotNull;

import java.util.Objects;

public class CreateReviewRequestDto {

    @NotNull
    private String filmTitle;
    private int score;
    @NotNull
    private String text;

    public CreateReviewRequestDto(String filmTitle, int score, String text) {
        this.filmTitle = filmTitle;
        this.score = score;
        this.text = text;
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
        return "CreateReviewRequestDto{" +
                "filmTitle='" + filmTitle + '\'' +
                ", score=" + score +
                ", text='" + text + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CreateReviewRequestDto that = (CreateReviewRequestDto) o;
        return score == that.score && Objects.equals(filmTitle, that.filmTitle) && Objects.equals(text, that.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(filmTitle, score, text);
    }
}
