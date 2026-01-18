package com.edu.film_database.dto.response;

import java.util.List;
import java.util.Objects;

public class FilmReviewResponseDto {

    private String title;
    private List<ReviewResponseDto> reviews;

    public FilmReviewResponseDto(String title, List<ReviewResponseDto> reviews) {
        this.title = title;
        this.reviews = reviews;
    }

    public String getTitle() {
        return title;
    }

    public List<ReviewResponseDto> getReviews() {
        return reviews;
    }

    @Override
    public String toString() {
        return "FilmReviewResponseDto{" +
                "title='" + title + '\'' +
                ", reviews=" + reviews +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        FilmReviewResponseDto that = (FilmReviewResponseDto) o;
        return Objects.equals(title, that.title) && Objects.equals(reviews, that.reviews);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, reviews);
    }
}
