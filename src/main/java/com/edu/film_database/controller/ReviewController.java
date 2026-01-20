package com.edu.film_database.controller;

import com.edu.film_database.api.ReviewApi;
import com.edu.film_database.dto.request.CreateReviewRequestDto;
import com.edu.film_database.dto.request.UpdateReviewRequestDto;
import com.edu.film_database.dto.response.FilmReviewResponseDto;
import com.edu.film_database.service.ReviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
public class ReviewController implements ReviewApi {

    private final ReviewService service;

    public ReviewController(ReviewService service) {
        this.service = service;
    }

    @Override
    public ResponseEntity<List<FilmReviewResponseDto>> getAllReviews(){
        return ResponseEntity.ok(service.getAllReviews());
    }

    @Override
    public ResponseEntity<FilmReviewResponseDto> getReviewByFilm(
            @PathVariable int filmId){
        return ResponseEntity.ok(service.getByFilm(filmId));
    }

    @Override
    public ResponseEntity<List<FilmReviewResponseDto>> getReviewByUser(
            @PathVariable String email){
        return ResponseEntity.ok(service.getByUserName(email));
    }

    @Override
    public ResponseEntity<FilmReviewResponseDto> createReview(@RequestBody CreateReviewRequestDto dto, Principal principal){
        return ResponseEntity.ok(service.createReview(principal, dto));
    }

    @Override
    public ResponseEntity<FilmReviewResponseDto> updateReview(@RequestBody UpdateReviewRequestDto dto, Principal principal){
        return ResponseEntity.ok(service.updateReview(principal, dto));
    }

    @Override
    public ResponseEntity<FilmReviewResponseDto> deleteReviewUser(@PathVariable int index, Principal principal){
        return ResponseEntity.ok(service.deleteReviewUser(index, principal));
    }

    @Override
    public ResponseEntity<FilmReviewResponseDto> deleteReviewAdmin(@PathVariable int index){
        return ResponseEntity.ok(service.deleteReviewAdmin(index));
    }
}
