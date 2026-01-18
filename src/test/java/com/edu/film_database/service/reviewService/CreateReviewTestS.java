package com.edu.film_database.service.reviewService;

import com.edu.film_database.dto.request.CreateReviewRequestDto;
import com.edu.film_database.dto.response.FilmReviewResponseDto;
import com.edu.film_database.dto.response.ReviewResponseDto;
import com.edu.film_database.exception.FilmNotFoundException;
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
public class CreateReviewTestS {

    @Mock
    private ReviewRepository review_repo;

    @Mock
    private FilmRepository film_repo;

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
    private CreateReviewRequestDto dto;
    private Principal principal;


    @BeforeEach
    public void setUp(){
        film = new Film();
        film.setTitle("testFilm");

        user = new User();
        user.setUsername("testName");
        user.setEmail("testName@somedomain.com");

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
        response_f.add(new FilmReviewResponseDto(film.getTitle(), response_r));

        films = new ArrayList<>();
        films.add(film);

        dto = new CreateReviewRequestDto(film.getTitle(), review.getScore(), review.getText());

        principal = new UserPrincipal(user.getEmail());
    }

    @AfterEach
    public void clean(){
        response_r = null;
        response_f = null;
        films = null;
        film = null;
        review = null;
        user = null;
        dto = null;
    }

    @Test
    @DisplayName("CreateReview with matching film present, should return confirmation on review added")
    public void createReviewFilmPresent(){
        when(film_repo.findByTitle(dto.getFilmTitle())).thenReturn(Optional.of(film));
        when(user_repo.findByEmail(principal.getName())).thenReturn(Optional.of(user));
        when(film_repo.findByTitle(film.getTitle())).thenReturn(Optional.of(film));

        String result = review_service.createReview(principal, dto);

        assertEquals(result, "Review for film " + dto.getFilmTitle() + " has been added");
    }

    @Test
    @DisplayName("CreateReview with no matching film present, should throw FilmNotFoundException")
    public void createReviewException(){
        when(film_repo.findByTitle(dto.getFilmTitle())).thenReturn(Optional.empty());

        FilmNotFoundException exception = assertThrows(FilmNotFoundException.class,
                () -> review_service.createReview(principal, dto));
        assertEquals("Cannot find the film named " + dto.getFilmTitle(), exception.getMessage());
    }
}
