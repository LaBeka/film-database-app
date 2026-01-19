package com.edu.film_database.service.reviewService;

import com.edu.film_database.dto.request.CreateReviewRequestDto;
import com.edu.film_database.dto.request.UpdateReviewRequestDto;
import com.edu.film_database.dto.response.FilmReviewResponseDto;
import com.edu.film_database.dto.response.ReviewResponseDto;
import com.edu.film_database.exception.FilmNotFoundException;
import com.edu.film_database.exception.ReviewNotFoundException;
import com.edu.film_database.exception.ReviewNotUsersOwnReviewException;
import com.edu.film_database.model.Film;
import com.edu.film_database.model.Review;
import com.edu.film_database.model.User;
import com.edu.film_database.repo.FilmRepository;
import com.edu.film_database.repo.ReviewRepository;
import com.edu.film_database.repo.UserRepository;
import com.edu.film_database.service.ReviewService;
import com.sun.security.auth.UserPrincipal;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UpdateReviewTest {

    @Mock
    private ReviewRepository review_repo;

    @Mock
    private UserRepository user_repo;

    @Spy
    @InjectMocks
    ReviewService review_service;

    //test data
    private List<FilmReviewResponseDto> response_f;
    private List<ReviewResponseDto> response_r;
    private List<Film> films;
    private Film film;
    private Review review;
    private User user;
    private User other_user;
    private UpdateReviewRequestDto dto;
    private Principal principal;
    private Principal principal_other;


    @BeforeEach
    public void setUp(){
        film = new Film();
        film.setTitle("testFilm");

        user = new User();
        user.setUsername("testName");
        user.setEmail("testName@somedomain.com");

        other_user = new User();
        user.setUsername("testName1");
        user.setEmail("testName1@somedomain.com");

        review = new Review();
        review.setId(1);
        review.setText("testReview");
        review.setDate(LocalDate.now());
        review.setScore(5);
        review.setUser(user);
        review.setFilm(film);

        response_r = new ArrayList<>();
        response_r.add(new ReviewResponseDto(review.getId(), user.getUsername(), review.getText(),
                review.getDate(), review.getScore()));

        response_f = new ArrayList<>();
        response_f.add(new FilmReviewResponseDto(film.getId(), response_r));

        films = new ArrayList<>();
        films.add(film);

        dto = new UpdateReviewRequestDto(1, film.getTitle(), review.getScore(), review.getText());

        principal = new UserPrincipal(user.getEmail());
        principal_other = new UserPrincipal("testName1@somedomain.com");
    }

    @AfterEach
    public void clean(){
        response_r = null;
        response_f = null;
        films = null;
        film = null;
        review = null;
        user = null;
        other_user = null;
        dto = null;
    }

    @Test
    @DisplayName("UpdateReview with review by specified user making the update," +
            "should return confirmation of update")
    public void updateReviewPresent(){
        when(user_repo.findByEmail(principal.getName())).thenReturn(Optional.of(user));
        when(review_repo.findById(dto.getReviewIndex())).thenReturn(Optional.of(review));

        FilmReviewResponseDto result = review_service.updateReview(principal, dto);

        assertEquals(result, response_f.get(0));
    }

    @Test
    @DisplayName("UpdateReview with no review by specified user making the update," +
            "should throw ReviewNotFoundException")
    public void updateReviewNotPresent(){
        when(user_repo.findByEmail(principal.getName())).thenReturn(Optional.of(user));
        when(review_repo.findById(dto.getReviewIndex())).thenReturn(Optional.empty());

        ReviewNotFoundException exception = assertThrows(ReviewNotFoundException.class,
                () -> review_service.updateReview(principal, dto));
        assertEquals("Cannot update non existing review", exception.getMessage());
    }

    @Test
    @DisplayName("UpdateReview trying to update review not by specified user," +
            "should throw ReviewNotUsersOwnReviewException")
    public void updateReviewNotUsersOwn(){
        when(user_repo.findByEmail(principal_other.getName())).thenReturn(Optional.of(other_user));
        when(review_repo.findById(dto.getReviewIndex())).thenReturn(Optional.of(review));

        ReviewNotUsersOwnReviewException exception = assertThrows(ReviewNotUsersOwnReviewException.class,
                () -> review_service.updateReview(principal_other, dto));
        assertEquals("Cannot change other users review", exception.getMessage());
    }
}
