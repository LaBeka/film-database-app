package com.edu.film_database.controller;

import com.edu.film_database.api.FilmApi;
import com.edu.film_database.dto.request.FilmRequestDTO;
import com.edu.film_database.dto.response.FilmResponseDTO;
import com.edu.film_database.service.FilmService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
public class FilmController implements FilmApi {

    @Autowired
    FilmService service;

    @Override
    public ResponseEntity<String> status(){
        return ResponseEntity.ok("OK");
    }

    @Override
    public ResponseEntity<List<FilmResponseDTO>> findAll(){
        return ResponseEntity.ok(service.findAll());
    }

    @Override
    public ResponseEntity<FilmResponseDTO> findFilmbyId(@PathVariable int id){
        return ResponseEntity.ok(service.findById(id));
    }

    @Override
    public ResponseEntity<List<FilmResponseDTO>> findByTitle(@PathVariable String title){
        return ResponseEntity.ok(service.findByTitle(title));
    }

    @Override
    public ResponseEntity<List<FilmResponseDTO>> searchByTitle(@PathVariable String title){
        return ResponseEntity.ok(service.searchByTitle(title));
    }

    @Override
    public ResponseEntity<List<FilmResponseDTO>> findByGenre(@PathVariable String genre){
        return ResponseEntity.ok(service.searchByGenre(genre) );
    }


    @Override
    public  ResponseEntity<List<FilmResponseDTO>> searchByActor(@PathVariable String actor){
           return ResponseEntity.ok(service.searchByActor(actor));
    }

    @Override
    public ResponseEntity<FilmResponseDTO> createFilm(@Valid @RequestBody FilmRequestDTO req, Principal principal ){
        return ResponseEntity.ok(service.createFilm(req));
    }
}