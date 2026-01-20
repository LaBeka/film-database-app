package com.edu.film_database.controller;

import com.edu.film_database.api.TestingApi;
import com.edu.film_database.model.Film;
import com.edu.film_database.repo.FilmRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TestController implements TestingApi {

    private final FilmRepository repository;

    @Override
    public ResponseEntity<String> getData() {
        return new ResponseEntity<>("Connection with backend is sucessful", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<Film>> getFilms() {
        List<Film> allFilms = repository.findAll();
        return new ResponseEntity<List<Film>>(allFilms, HttpStatus.OK);
    }
}
