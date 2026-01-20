package com.edu.film_database.dto.response;

import java.util.List;
import java.util.Objects;

public class FilmReviewResponseDto {

    private int filmId;
    private List<ReviewResponseDto> reviews;

    public FilmReviewResponseDto(int filmId, List<ReviewResponseDto> reviews) {
        this.filmId = filmId;
        this.reviews = reviews;
    }

    public int getFilmId() {
        return filmId;
    }

    public List<ReviewResponseDto> getReviews() {
        return reviews;
    }

    @Override
    public String toString() {
        return "FilmReviewResponseDto{" +
                "filmId=" + filmId +
                ", reviews=" + reviews +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        FilmReviewResponseDto that = (FilmReviewResponseDto) o;
        return filmId == that.filmId && Objects.equals(reviews, that.reviews);
    }

    @Override
    public int hashCode() {
        return Objects.hash(filmId, reviews);
    }
}
