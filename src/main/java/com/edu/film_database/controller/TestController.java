package com.edu.film_database.controller;

import com.edu.film_database.api.TestingApi;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController implements TestingApi {

    @Override
    public ResponseEntity<String> getData() {
        return new ResponseEntity<>("Hello form test controller UPDATED!!!!! Beka", HttpStatus.OK);
    }
}
