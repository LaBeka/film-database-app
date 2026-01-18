package com.edu.film_database.service.reviewService;

import com.edu.film_database.dto.response.FilmReviewResponseDto;
import com.edu.film_database.dto.response.ReviewResponseDto;
import com.edu.film_database.exception.FilmNotFoundException;
import com.edu.film_database.model.Film;
import com.edu.film_database.model.Review;
import com.edu.film_database.model.User;
import com.edu.film_database.repo.FilmRepository;
import com.edu.film_database.repo.ReviewRepository;
import com.edu.film_database.service.ReviewService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GetByFilmTestS {

    @Mock
    private ReviewRepository review_repo;

    @Mock
    private FilmRepository film_repo;

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


    @BeforeEach
    public void setUp(){
        film = new Film();
        film.setTitle("testFilm");

        user = new User();
        user.setUsername("testName");

        review = new Review();
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
    }

    @AfterEach
    public void clean(){
        response_r = null;
        response_f = null;
        films = null;
        film = null;
        review = null;
        user = null;
    }

    @Test
    @DisplayName("GetByFilm with matching review present, should return review")
    public void getByFilmPresent(){
        when(film_repo.findByTitle(film.getTitle())).thenReturn(Optional.of(film));
        when(review_repo.findByFilm(film)).thenReturn(List.of(review));

        FilmReviewResponseDto result = review_service.getByFilm(film.getTitle());

        assertEquals(result, response_f.get(0));
    }

    @Test
    @DisplayName("GetGByFilm with film with no reviews present," +
            " should return filmTitle and no reviews")
    public void getByFilmNotPresent(){
        response_f.get(0).getReviews().remove(0);

        when(film_repo.findByTitle(film.getTitle())).thenReturn(Optional.of(film));
        when(review_repo.findByFilm(film)).thenReturn(List.of());

        FilmReviewResponseDto result = review_service.getByFilm(film.getTitle());

        assertEquals(result, response_f.get(0));
    }

    @Test
    @DisplayName("GetGByFilm with no matching film present," +
            " should throw FilmNotFoundException")
    public void getByFilmException(){
        response_f.remove(0);

        when(film_repo.findByTitle(film.getTitle())).thenReturn(Optional.empty());

        FilmNotFoundException exception = assertThrows(FilmNotFoundException.class,
                () -> review_service.getByFilm(film.getTitle()));
        assertEquals("Cannot find the film named " + film.getTitle(), exception.getMessage());
    }
}
