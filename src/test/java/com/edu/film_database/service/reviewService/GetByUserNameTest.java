package com.edu.film_database.service.reviewService;

import com.edu.film_database.dto.response.FilmReviewResponseDto;
import com.edu.film_database.dto.response.ReviewResponseDto;
import com.edu.film_database.exception.FilmNotFoundException;
import com.edu.film_database.exception.UserNotFoundException;
import com.edu.film_database.model.Film;
import com.edu.film_database.model.Review;
import com.edu.film_database.model.User;
import com.edu.film_database.repo.FilmRepository;
import com.edu.film_database.repo.ReviewRepository;
import com.edu.film_database.repo.UserRepository;
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
public class GetByUserNameTest {

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
        response_r.add(new ReviewResponseDto(review.getId(), user.getUsername(), user.getEmail(), review.getText(),
                review.getDate(), review.getScore()));

        response_f = new ArrayList<>();
        response_f.add(new FilmReviewResponseDto(film.getId(), response_r));

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
    @DisplayName("GetByUserName with review by specified user present, should return review")
    public void getByUserNamePresent(){
        when(user_repo.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(review_repo.findByUser(user)).thenReturn(List.of(review));
        when(review_repo.findByUserAndFilm(user, film)).thenReturn(List.of(review));
        when(review_repo.findById(review.getId())).thenReturn(Optional.of(review));

        List<FilmReviewResponseDto> result = review_service.getByUserName(user.getEmail());

        assertEquals(result.get(0), response_f.get(0));
    }

    @Test
    @DisplayName("GetByUserName with review by specified user not present," +
            " should return an empty list")
    public void getByUserNameNotPresent(){
        response_f.get(0).getReviews().remove(0);

        when(user_repo.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        List<FilmReviewResponseDto> result = review_service.getByUserName(user.getEmail());

        assertEquals(result, new ArrayList<>());
    }

    @Test
    @DisplayName("GetByUserName with invalid user, should throw UserNotFoundException")
    public void getByUserNameException(){
        when(user_repo.findByEmail("notTestName@somedomain.com")).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class,
                () -> review_service.getByUserName("notTestName@somedomain.com"));
        assertEquals("Cannot find the user with email notTestName@somedomain.com",
                exception.getMessage());
    }
}
