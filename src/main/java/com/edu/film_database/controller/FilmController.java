package com.edu.film_database.controller;

import com.edu.film_database.model.dto.FilmResponseDTO;
import com.edu.film_database.service.FilmService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.ArrayList;
import java.util.List;


@RequestMapping("/film")
@RestController
public class FilmController {

    FilmService service;

    @GetMapping("/all")
    public ResponseEntity<List<FilmResponseDTO>> findAll(){
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/findbyid/{id}")
    public ResponseEntity<FilmResponseDTO> findFilmbyId(@PathVariable int id){
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping("/findbytitle/{title}")
    public ResponseEntity<List<FilmResponseDTO>> findByTitle(@PathVariable String title){
        return ResponseEntity.ok(service.findByTitle(title));
    }

    @GetMapping("/searchbytitle/{title}")
    public ResponseEntity<List<FilmResponseDTO>> searchByTitle(@PathVariable String title){
        return ResponseEntity.ok(service.searchByTitle(title));
    }

    @GetMapping("/findbygenre/{genre}")
    public ResponseEntity<List<FilmResponseDTO>> findByGenre(@PathVariable String genre){
        return ResponseEntity.ok(new ArrayList<>() );
    }

    @GetMapping("/serachbyactor/{actor}")
    public  ResponseEntity<List<FilmResponseDTO>> searchByActor(@PathVariable String actor){
           return ResponseEntity.ok(service.searchByActor(actor));
    }
}