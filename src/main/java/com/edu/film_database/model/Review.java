package com.edu.film_database.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "reviews")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String text;
    private LocalDate date;
    private int score;

    @ManyToOne
    @JoinColumn(name = "userId")
    @JsonManagedReference
    private User user;

    @ManyToOne
    @JoinColumn(name = "filmId")
    @JsonManagedReference
    private Film film;

    public Review() {
    }

    public Review(int id, String text, LocalDate date, int score, User user, Film film) {
        this.id = id;
        this.text = text;
        this.date = date;
        this.score = score;
        this.user = user;
        this.film = film;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Film getFilm() {
        return film;
    }

    public void setFilm(Film film) {
        this.film = film;
    }

    @Override
    public String toString() {
        return "Review{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", date=" + date +
                ", score=" + score +
                ", user=" + user +
                ", film=" + film +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Review review = (Review) o;
        return id == review.id && score == review.score && Objects.equals(text, review.text) && Objects.equals(date, review.date) && Objects.equals(user, review.user) && Objects.equals(film, review.film);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, text, date, score, user, film);
    }
}
