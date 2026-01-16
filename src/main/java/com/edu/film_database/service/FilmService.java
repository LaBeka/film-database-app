package com.edu.film_database.service;

import com.edu.film_database.model.Film;
import com.edu.film_database.model.dto.FilmResponseDTO;
import com.edu.film_database.repo.FilmRepository;

import java.util.List;

public class FilmService {

    FilmRepository repo;

    public List<FilmResponseDTO> findAll(){
        return repo.findAll().stream().map( f -> new FilmResponseDTO(
            f.getId(),
            f.getReleaseYear(),
            f.getTitle(),
            f.getGenre(),
            f.getCast(),
            f.getAgeRestriction(),
            f.getAwards(),
            f.getLanguages(),
            f.getAspectRatio(),
            f.getColorStatus(),
            f.getCameraUsed()
        )).toList();
    }

    public FilmResponseDTO findById(int id){
        Film film = repo.findById(id);
        return new FilmResponseDTO(
            film.getId(),
            film.getReleaseYear(),
            film.getTitle(),
            film.getGenre(),
            film.getCast(),
            film.getAgeRestriction(),
            film.getAwards(),
            film.getLanguages(),
            film.getAspectRatio(),
            film.getColorStatus(),
            film.getCameraUsed()
        );

    }

    public List<FilmResponseDTO> findByTitle(String sQuery){
        return repo.findByTitle(sQuery).stream().map( f -> new FilmResponseDTO(
            f.getId(),
            f.getReleaseYear(),
            f.getTitle(),
            f.getGenre(),
            f.getCast(),
            f.getAgeRestriction(),
            f.getAwards(),
            f.getLanguages(),
            f.getAspectRatio(),
            f.getColorStatus(),
            f.getCameraUsed()
        )).toList();
    }

    public List<FilmResponseDTO> searchByTitle(String sQuery){
        return repo.findByTitle(sQuery).stream().map( f -> new FilmResponseDTO(
            f.getId(),
            f.getReleaseYear(),
            f.getTitle(),
            f.getGenre(),
            f.getCast(),
            f.getAgeRestriction(),
            f.getAwards(),
            f.getLanguages(),
            f.getAspectRatio(),
            f.getColorStatus(),
            f.getCameraUsed()
        )).toList();
    }

    public List<FilmResponseDTO> searchByActor(String actor){
        return repo.searchByActor(actor).stream().map(f -> new FilmResponseDTO(
            f.getId(),
            f.getReleaseYear(),
            f.getTitle(),
            f.getGenre(),
            f.getCast(),
            f.getAgeRestriction(),
            f.getAwards(),
            f.getLanguages(),
            f.getAspectRatio(),
            f.getColorStatus(),
            f.getCameraUsed()
        )).toList();
    }



}