package com.edu.film_database.controller.reviewController;

import com.edu.film_database.model.Film;
import com.edu.film_database.model.Review;
import com.edu.film_database.model.Role;
import com.edu.film_database.model.User;
import com.edu.film_database.repo.FilmRepository;
import com.edu.film_database.repo.ReviewRepository;
import com.edu.film_database.repo.RoleRepository;
import com.edu.film_database.repo.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class GetReviewByFilmTestC {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RoleRepository role_repo;

    @Autowired
    private UserRepository user_repo;

    @Autowired
    private FilmRepository film_repo;

    @Autowired
    private ReviewRepository review_repo;

    //Test data
    private Role role;
    private User user;
    private Film film;
    private Review review;

    @BeforeEach
    public void setUp() {
        role_repo.deleteAll();
        user_repo.deleteAll();
        film_repo.deleteAll();
        review_repo.deleteAll();

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

    @Test
    @DisplayName("getReview with 1 matching review present, should return status 200 and the review")
    public void getReviewByFilmPresent() throws Exception {
        mockMvc.perform(get("/api/review/public/getByFilm/testFilm"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("testFilm"))
                .andExpect(jsonPath("$.reviews.[0].text").value("test-text"))
                .andExpect(jsonPath("$.reviews.[0].score").value(5));
    }

    @Test
    @DisplayName("getReview with no matching review present, should return status 404 and a message")
    public void getReviewByFilmNoMatches() throws Exception {
        mockMvc.perform(get("/api/review/public/getByFilm/notTestFilm"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message")
                        .value("Cannot find the film named notTestFilm"));
    }
}
