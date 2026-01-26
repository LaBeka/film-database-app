package com.edu.film_database.api;

import com.edu.film_database.dto.request.FilmRequestDTO;
import com.edu.film_database.dto.response.FilmResponseDTO;
import com.edu.film_database.service.FilmService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RequestMapping(com.edu.film_database.api.FilmApi.API_PATH_DICTIONARY)
@Tag(name = "Methods to work with Film", description = com.edu.film_database.api.FilmApi.API_PATH_DICTIONARY)
@Validated
public interface FilmApi {

    String API_PATH_DICTIONARY = "/api/film";

    @GetMapping("/status")
    public ResponseEntity<String> status();

    @GetMapping("/all")
    public ResponseEntity<List<FilmResponseDTO>> findAll();

    @GetMapping("/id/{id}")
    public ResponseEntity<FilmResponseDTO> findFilmbyId(@PathVariable int id);


    @GetMapping("/title/{title}")
    public ResponseEntity<List<FilmResponseDTO>> findByTitle(@PathVariable String title);

    @GetMapping("/search/{title}")
    public ResponseEntity<List<FilmResponseDTO>> searchByTitle(@PathVariable String title);

    @GetMapping("/genre/{genre}")
    public ResponseEntity<List<FilmResponseDTO>> findByGenre(@PathVariable String genre);


    @GetMapping("/actor/{actor}")
    public  ResponseEntity<List<FilmResponseDTO>> searchByActor(@PathVariable String actor);

    @PostMapping("/create")
    public ResponseEntity<FilmResponseDTO> createFilm(@Valid @RequestBody FilmRequestDTO req, Principal principal );
}
