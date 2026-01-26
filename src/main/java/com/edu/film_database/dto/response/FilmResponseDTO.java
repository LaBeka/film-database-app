package com.edu.film_database.dto.response;

public class FilmResponseDTO {

    private int id;
    Integer releaseYear;
    String title;
    String genre;
    String casta;
    Integer ageRestriction;
    String awards;
    String languages;
    String aspectRatio;
    String color;
    String camera;

    public FilmResponseDTO() {
    }

    public FilmResponseDTO(int id, Integer releaseYear, String title, String genre, String casta, Integer ageRestriction, String awards, String languages, String aspectRatio, String color, String camera) {
        this.id = id;
        this.title = title;
        this.releaseYear = releaseYear;
        this.genre = genre;
        this.casta = casta;
        this.ageRestriction = ageRestriction;
        this.awards = awards;
        this.languages = languages;
        this.aspectRatio = aspectRatio;
        this.color = color;
        this.camera = camera;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(Integer releaseYear) {
        this.releaseYear = releaseYear;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getCasta() {
        return casta;
    }

    public void setCasta(String casta) {
        this.casta = casta;
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
}