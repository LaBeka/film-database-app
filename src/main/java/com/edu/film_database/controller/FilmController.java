package com.edu.film_database.controller;

import com.edu.film_database.dto.response.FilmResponseDTO;
import com.edu.film_database.service.FilmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RequestMapping("/film")
@RestController
public class FilmController {

    @Autowired
    FilmService service;

    @GetMapping("/status")
    public ResponseEntity<String> status(){
        return ResponseEntity.ok("OK");
    }

    @GetMapping("/all")
    public ResponseEntity<List<FilmResponseDTO>> findAll(){
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<FilmResponseDTO> findFilmbyId(@PathVariable int id){
        return ResponseEntity.ok(service.findById(id));
    }


    @GetMapping("/title/{title}")
    public ResponseEntity<List<FilmResponseDTO>> findByTitle(@PathVariable String title){
        return ResponseEntity.ok(service.findByTitle(title));
    }

    @GetMapping("/search/{title}")
    public ResponseEntity<List<FilmResponseDTO>> searchByTitle(@PathVariable String title){
        return ResponseEntity.ok(service.searchByTitle(title));
    }

    @GetMapping("/genre/{genre}")
    public ResponseEntity<List<FilmResponseDTO>> findByGenre(@PathVariable String genre){
        return ResponseEntity.ok(service.searchByGenre(genre) );
    }


    @GetMapping("/actor/{actor}")
    public  ResponseEntity<List<FilmResponseDTO>> searchByActor(@PathVariable String actor){
           return ResponseEntity.ok(service.searchByActor(actor));
    }

}