package com.edu.film_database.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "films")
public class Film {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    String title;
    int releaseYear;
    String genre;
    String cast;
    @Column(name = "age_restriction", nullable = true, unique = false)
    int ageRestriction;
    @Column(name = "awards", nullable = true, unique = false)
    String awards;
    @Column(name = "languages", nullable = true, unique = false)
    String languages;
    @Column(name = "aspect_ratio", nullable = true, unique = false)
    String aspectRatio;
    @Column(name = "color", nullable = true, unique = false)
    String color;
    @Column(name = "camera", nullable = true, unique = false)
    String camera;

    @OneToMany(mappedBy = "film", cascade = CascadeType.ALL)
    @JsonBackReference
    private List<Review> reviews;

    public Film(Integer id, String title, Integer releaseYear, String genre, String cast, Integer ageRestriction, String awards, String languages, String aspectRatio, String colorStatus, String cameraUsed, List<Review> reviews) {
        this.id = id;
        this.title = title;
        this.releaseYear = releaseYear;
        this.genre = genre;
        this.cast = cast;
        this.ageRestriction = ageRestriction;
        this.awards = awards;
        this.languages = languages;
        this.aspectRatio = aspectRatio;
        this.color = colorStatus;
        this.camera = cameraUsed;
        this.reviews = reviews;
    }

    public Film() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public Integer getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(Integer releaseYear) {
        this.releaseYear = releaseYear;
    }

    public void setLanguages(String languages) {
        this.languages = languages;
    }


    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getCast() {
        return cast;
    }

    public void setCast(String cast) {
        this.cast = cast;
    }

    public Integer getAgeRestriction() {
        return ageRestriction;
    }

    public void setAgeRestriction(Integer ageRestriction) {
        this.ageRestriction = ageRestriction;
    }

    public String getAwards() {
        return awards;
    }

    public void setAwards(String awards) {
        this.awards = awards;
    }

    public String getLanguages() {
        return languages;
    }

    public void setLanguage(String langusges) {
        this.languages = langusges;
    }

    public String getAspectRatio() {
        return aspectRatio;
    }

    public void setAspectRatio(String aspectRatio) {
        this.aspectRatio = aspectRatio;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getCamera() {
        return camera;
    }

    public void setCamera(String camera) {
        this.camera = camera;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }
}