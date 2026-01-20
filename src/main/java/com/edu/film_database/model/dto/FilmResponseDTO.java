package com.edu.film_database.model.dto;

import java.util.List;

public class FilmResponseDTO {

    private int id;
    Integer releaseYear;
    String title;
    String genre;
    String cast;
    Integer ageRestriction;
    String awards;
    String languages;
    String aspectRatio;
    String colorStatus;
    String cameraUsed;

    public FilmResponseDTO() {
    }

    public FilmResponseDTO(Integer id, String title, Integer releaseYear, String genre, String cast, Integer ageRestriction, String awards, String languages,
                           String aspectRatio, String colorStatus, String cameraUsed) {
        this.id = id;
        this.title = title;
        this.releaseYear = releaseYear;
        this.genre = genre;
        this.cast = cast;
        this.ageRestriction = ageRestriction;
        this.awards = awards;
        this.languages = languages;
        this.aspectRatio = aspectRatio;
        this.colorStatus = colorStatus;
        this.cameraUsed = cameraUsed;
    }

    public Integer getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(Integer releaseYear) {
        this.releaseYear = releaseYear;
    }

    public void setAspectRatio(String aspectRatio) {
        this.aspectRatio = aspectRatio;
    }

    public void setId(int id) {
        this.id = id;
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

    public Integer getReleaseDate() {
        return releaseYear;
    }

    public void setReleaseDate(Integer releaseDate) {
        this.releaseYear = releaseDate;
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

    public void setLanguages(String languages) {
        this.languages = languages;
    }

    public String getAspectRatio() {
        return aspectRatio;
    }

    public String getColorStatus() {
        return colorStatus;
    }

    public void setColorStatus(String colorStatus) {
        this.colorStatus = colorStatus;
    }

    public String getCameraUsed() {
        return cameraUsed;
    }

    public void setCameraUsed(String cameraUsed) {
        this.cameraUsed = cameraUsed;
    }
}