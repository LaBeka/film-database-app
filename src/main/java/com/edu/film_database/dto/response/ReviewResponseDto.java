package com.edu.film_database.dto.response;

import java.time.LocalDate;
import java.util.Objects;

public class ReviewResponseDto {

    private int index;
    private String userName;
    private String email;
    private String text;
    private LocalDate date;
    private int score;

    public ReviewResponseDto(int index, String userName, String email, String text, LocalDate date, int score) {
        this.index = index;
        this.userName = userName;
        this.email = email;
        this.text = text;
        this.date = date;
        this.score = score;
    }

    public int getIndex() {
        return index;
    }

    public String getUserName() {
        return userName;
    }

    public String getEmail() {
        return email;
    }

    public String getText() {
        return text;
    }

    public LocalDate getDate() {
        return date;
    }

    public int getScore() {
        return score;
    }

    @Override
    public String toString() {
        return "ReviewResponseDto{" +
                "index=" + index +
                ", userName='" + userName + '\'' +
                ", email='" + email + '\'' +
                ", text='" + text + '\'' +
                ", date=" + date +
                ", score=" + score +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ReviewResponseDto that = (ReviewResponseDto) o;
        return index == that.index && score == that.score && Objects.equals(userName, that.userName) && Objects.equals(email, that.email) && Objects.equals(text, that.text) && Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(index, userName, email, text, date, score);
    }
}
