package com.edu.film_database.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "films")
public class Film {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    String title;
    String genre;
    List<String> cast;
    int ageRestriction;
    List<String> awards;
    List<String> langusges;
    double aspectRatio;
    String colorStatus;
    String cameraUsed;

    public Film(int id, String title, String genre, List<String> cast, int ageRestriction, List<String> awards, List<String> langusges, double aspectRatio, String colorStatus, String cameraUsed) {
        this.id = id;
        this.title = title;
        this.genre = genre;
        this.cast = cast;
        this.ageRestriction = ageRestriction;
        this.awards = awards;
        this.langusges = langusges;
        this.aspectRatio = aspectRatio;
        this.colorStatus = colorStatus;
        this.cameraUsed = cameraUsed;
    }

    public Film() {
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

    public List<String> getLangusges() {
        return langusges;
    }

    public void setLangusges(List<String> langusges) {
        this.langusges = langusges;
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