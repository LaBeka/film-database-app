package com.edu.film_database.api;

import com.edu.film_database.model.Film;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping(com.edu.film_database.api.TestingApi.API_TEST_DICTIONARY)
@Tag(name = "Api for testing general url", description = com.edu.film_database.api.TestingApi.API_TEST_DICTIONARY)
@Validated
public interface TestingApi {

    String API_TEST_DICTIONARY = "/api/test";

    @GetMapping("/")
    @Operation(summary = "Health check of backend for frontend landing page")
    ResponseEntity<String> getData();

    @GetMapping("/films")
    @Operation(summary = "List of films")
    ResponseEntity<List<Film>> getFilms();
}
