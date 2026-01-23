package com.edu.film_database.api;

import com.edu.film_database.dto.request.CreateReviewRequestDto;
import com.edu.film_database.dto.request.UpdateReviewRequestDto;
import com.edu.film_database.dto.response.FilmReviewResponseDto;
import com.edu.film_database.dto.response.ReviewResponseDto;
import com.edu.film_database.dto.response.UserResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RequestMapping(com.edu.film_database.api.ReviewApi.API_PATH_DICTIONARY)
@Tag(name = "Methods to work with Review", description = com.edu.film_database.api.ReviewApi.API_PATH_DICTIONARY)
@Validated
public interface ReviewApi {

    String API_PATH_DICTIONARY = "/api/review";

    @GetMapping("public/getAllReviews")
    @Operation(summary = "List of all the reviews")
    public ResponseEntity<List<FilmReviewResponseDto>> getAllReviews();

    @GetMapping("/public/getByFilm/{filmId}")
    @Operation(summary = "List of all the reviews based on filmTitle")
    public ResponseEntity<FilmReviewResponseDto> getReviewByFilm(
            @PathVariable int filmId);

    @GetMapping("user/getByUser/{email}")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "List of all the reviews based on specific user")
    public ResponseEntity<List<FilmReviewResponseDto>> getReviewByUser(
            @PathVariable String email);

    @PostMapping("user/createReview")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Create a new review")
    public ResponseEntity<FilmReviewResponseDto> createReview(@RequestBody CreateReviewRequestDto dto, Principal principal);

    @PatchMapping("user/updateReview")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Update an existing review")
    public ResponseEntity<FilmReviewResponseDto> updateReview(@RequestBody UpdateReviewRequestDto dto, Principal principal);

    @DeleteMapping("user/deleteReview/{index}")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Delete an existing review")
    public ResponseEntity<FilmReviewResponseDto> deleteReviewUser(@PathVariable int index, Principal principal);

    @DeleteMapping("admin/deleteReview/{index}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete an existing review")
    public ResponseEntity<FilmReviewResponseDto> deleteReviewAdmin(@PathVariable int index);

    @GetMapping("user/getSpecificReview/{index}")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Get a specific review")
    public ResponseEntity<ReviewResponseDto> getSpecificReview(@PathVariable int index);
}
