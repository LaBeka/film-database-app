package com.edu.film_database.repository.reviewRepository;

import com.edu.film_database.model.Film;
import com.edu.film_database.model.Review;
import com.edu.film_database.model.Role;
import com.edu.film_database.model.User;
import com.edu.film_database.repo.FilmRepository;
import com.edu.film_database.repo.ReviewRepository;
import com.edu.film_database.repo.RoleRepository;
import com.edu.film_database.repo.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
@DataJpaTest
public class FindByFilmTest {

    @Autowired
    private RoleRepository role_repo;

    @Autowired
    private UserRepository user_repo;

    @Autowired
    private FilmRepository film_repo;

    @Autowired
    private ReviewRepository review_repo;

    //test-data
    private Role role;
    private User user;
    private Film film;
    private Review review;

    @BeforeEach
    public void setUp(){
        role = new Role();
        role.setName("USER");
        role_repo.save(role);

        user = new User();
        user.setUsername("testUser");
        user.setEmail("testUser@somedomain.com");
        user.setPassword("test123");
        user.setFullName("testUserFullName");
        user.setAge(23);
        user.setCurrentlyActive(true);
        user.setRoles(Set.of(role));
        user_repo.save(user);

        film = new Film();
        film.setTitle("testFilm");
        film.setAgeRestriction(15);
        film.setAspectRatio(2.2);
        film_repo.save(film);

        review = new Review();
        review.setText("test-text");
        review.setDate(LocalDate.now());
        review.setScore(5);
        review.setUser(user);
        review.setFilm(film);
        review_repo.save(review);
    }

    @AfterEach
    public void clean(){
        role = null;
        user = null;
        film = null;
        review = null;

        review_repo.deleteAll();
        film_repo.deleteAll();
        user_repo.deleteAll();
        role_repo.deleteAll();
    }

    @Test
    @DisplayName("FindByFilm with review on specified film present, " +
            "should return review of specified film")
    public void findByFilmPresent(){
        List<Review> result = review_repo.findByFilm(film);

        assertEquals(1, result.size());
        assertEquals("test-text", result.get(0).getText());
        assertEquals(5, result.get(0).getScore());
    }

    @Test
    @DisplayName("FindByFilm with review on specified film not present, " +
            "should return empty list")
    public void findByFilmNotPresent(){
        review_repo.deleteAll();
        List<Review> result = review_repo.findByFilm(film);

        assertEquals(0, result.size());

    }
}
