package com.edu.film_database.api;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping(com.edu.film_database.api.TestingApi.API_TEST_DICTIONARY)
@Tag(name = "Api for testing general url", description = com.edu.film_database.api.TestingApi.API_TEST_DICTIONARY)
@Validated
public interface TestingApi {

    String API_TEST_DICTIONARY = "/api/test";

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/")
    ResponseEntity<String> getData();
}
