package com.edu.film_database.controller;

import com.edu.film_database.api.ReviewApi;
import com.edu.film_database.dto.request.CreateReviewRequestDto;
import com.edu.film_database.dto.request.UpdateReviewRequestDto;
import com.edu.film_database.dto.response.FilmReviewResponseDto;
import com.edu.film_database.service.ReviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/review")
public class ReviewController implements ReviewApi {

    private final ReviewService service;

    public ReviewController(ReviewService service) {
        this.service = service;
    }


    @GetMapping("user/getAllReviews")
    public ResponseEntity<List<FilmReviewResponseDto>> getAllReviews(){
        return ResponseEntity.ok(service.getAllReviews());
    }

    @GetMapping("user/getByFilm/{filmTitle}")
    public ResponseEntity<FilmReviewResponseDto> getReviewByFilm(
            @PathVariable String filmTitle){
        return ResponseEntity.ok(service.getByFilm(filmTitle));
    }

    @GetMapping("admin/getByUser/{userName}")
    public ResponseEntity<List<FilmReviewResponseDto>> getReviewByUser(
            @PathVariable String userName){
        return ResponseEntity.ok(service.getByUserName(userName));
    }

    @PostMapping("user/createReview")
    public ResponseEntity<String> createReview(@RequestBody CreateReviewRequestDto dto){
        return null;
    }

    @PatchMapping("user/updateReview")
    public ResponseEntity<String> updateReview(@RequestBody UpdateReviewRequestDto dto){
        return null;
    }

    @DeleteMapping("user/deleteReview/{index}")
    public ResponseEntity<String> deleteReviewUser(@PathVariable int index){
        return null;
    }

    @DeleteMapping("admin/deleteReview/{index}")
    public ResponseEntity<String> deleteReviewAdmin(@PathVariable int index){
        return null;
    }
}
