package com.edu.film_database.service;

import com.edu.film_database.model.Film;
import com.edu.film_database.dto.response.FilmResponseDTO;
import com.edu.film_database.repo.FilmRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FilmService {

    @Autowired
    FilmRepository repo;

    public List<FilmResponseDTO> findAll(){
        return repo.findAll().stream().map( f -> new FilmResponseDTO(
            f.getId(),
                f.getReleaseYear(),
                f.getTitle(),
            f.getGenre(),
            f.getCasta(),
            f.getAgeRestriction(),
//                Collections.emptyList(),
//                Collections.emptyList(),
            f.getAwards(),
            f.getLanguages(),
            f.getAspectRatio(),
            f.getColor(),
            f.getCamera()
                // == null ? "N/A"
        )).toList();
    }


    public FilmResponseDTO findById(int id){
        Optional<Film> filmOption = repo.findById(id);
        Film film = filmOption.orElseThrow();
        return new FilmResponseDTO(
            film.getId(),
                film.getReleaseYear(),
                film.getTitle(),
            film.getGenre(),
            film.getCasta(),
            film.getAgeRestriction(),
            film.getAwards(),
            film.getLanguages(),
            film.getAspectRatio(),
            film.getColor(),
            film.getCamera()
        );

    }

    public List<FilmResponseDTO> findByTitle(String sQuery){
        return repo.findByTitle(sQuery).stream().map( f -> new FilmResponseDTO(
            f.getId(),
                f.getReleaseYear(),
                f.getTitle(),
            f.getGenre(),
            f.getCasta(),
            f.getAgeRestriction(),
            f.getAwards(),
            f.getLanguages(),
            f.getAspectRatio(),
            f.getColor(),
            f.getCamera()
        )).toList();
    }

    public List<FilmResponseDTO> searchByTitle(String sQuery){
        return repo.findByTitle(sQuery).stream().map( f -> new FilmResponseDTO(
            f.getId(),
                f.getReleaseYear(),
                f.getTitle(),
            f.getGenre(),
            f.getCasta(),
            f.getAgeRestriction(),
            f.getAwards(),
            f.getLanguages(),
            f.getAspectRatio(),
            f.getColor(),
            f.getCamera()
        )).toList();
    }


    public List<FilmResponseDTO> searchByGenre(String sQuery){
        return repo.findByGenre(sQuery).stream().map( f -> new FilmResponseDTO(
            f.getId(),
                f.getReleaseYear(),
                f.getTitle(),
            f.getGenre(),
            f.getCasta(),
            f.getAgeRestriction(),
            f.getAwards(),
            f.getLanguages(),
            f.getAspectRatio(),
            f.getColor(),
            f.getCamera()
        )).toList();
    }


    public List<FilmResponseDTO> searchByActor(String actor){
        return repo.searchByActor(actor).stream().map(f -> new FilmResponseDTO(
            f.getId(),
                f.getReleaseYear(),
                f.getTitle(),
            f.getGenre(),
            f.getCasta(),
            f.getAgeRestriction(),
            f.getAwards(),
            f.getLanguages(),
            f.getAspectRatio(),
            f.getColor(),
            f.getCamera()
        )).toList();
    }
}