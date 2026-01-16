package com.edu.film_database.model.dto;

import java.util.List;

public class FilmResponseDTO {

    private int id;
    int releaseYear;
    String title;
    String genre;
    List<String> cast;
    int ageRestriction;
    List<String> awards;
    List<String> languages;
    double aspectRatio;
    String colorStatus;
    String cameraUsed;

    public FilmResponseDTO() {
    }

    public FilmResponseDTO(int id, int releaseYear, String title, String genre, List<String> cast, int ageRestriction, List<String> awards, List<String> languages, double aspectRatio, String colorStatus, String cameraUsed) {
        this.id = id;
        this.releaseYear = releaseYear;
        this.title = title;
        this.genre = genre;
        this.cast = cast;
        this.ageRestriction = ageRestriction;
        this.awards = awards;
        this.languages = languages;
        this.aspectRatio = aspectRatio;
        this.colorStatus = colorStatus;
        this.cameraUsed = cameraUsed;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getReleaseDate() {
        return releaseYear;
    }

    public void setReleaseDate(int releaseDate) {
        this.releaseYear = releaseDate;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public List<String> getCast() {
        return cast;
    }

    public void setCast(List<String> cast) {
        this.cast = cast;
    }

    public int getAgeRestriction() {
        return ageRestriction;
    }

    public void setAgeRestriction(int ageRestriction) {
        this.ageRestriction = ageRestriction;
    }

    public List<String> getAwards() {
        return awards;
    }

    public void setAwards(List<String> awards) {
        this.awards = awards;
    }

    public List<String> getLanguages() {
        return languages;
    }

    public void setLanguages(List<String> languages) {
        this.languages = languages;
    }

    public double getAspectRatio() {
        return aspectRatio;
    }

    public void setAspectRatio(double aspectRatio) {
        this.aspectRatio = aspectRatio;
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