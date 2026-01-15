package com.edu.film_database.controller.reviewController;

import com.edu.film_database.dto.request.CreateReviewRequestDto;
import com.edu.film_database.model.Film;
import com.edu.film_database.model.Review;
import com.edu.film_database.model.Role;
import com.edu.film_database.model.User;
import com.edu.film_database.repo.FilmRepository;
import com.edu.film_database.repo.ReviewRepository;
import com.edu.film_database.repo.RoleRepository;
import com.edu.film_database.repo.UserRepository;
import com.sun.security.auth.UserPrincipal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@SpringBootTest(properties = "spring.security.enabled=false")
@AutoConfigureMockMvc(addFilters = false)
public class CreateReviewTestC {

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
    private CreateReviewRequestDto dtoOk;
    private CreateReviewRequestDto dtoError;
    private Principal principal;

    @BeforeEach
    public void setUp() {

        review_repo.deleteAll();
        film_repo.deleteAll();
        user_repo.deleteAll();
        role_repo.deleteAll();

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

        Authentication auth =
                new UsernamePasswordAuthenticationToken(
                        "testUser", null, List.of(new SimpleGrantedAuthority("ROLE_USER"))
                );

        SecurityContextHolder.getContext().setAuthentication(auth);

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

        dtoOk = new CreateReviewRequestDto("testFilm", 5, "test-text-2");
        dtoError = new CreateReviewRequestDto("notTestFilm", 5, "test-text-2");
        principal = new UserPrincipal("testUser@somedomain.com");
    }

    @Test
    @DisplayName("create review with film present, should return status 200 and message")
    public void createReviewFilmPresent() throws Exception {
        review_repo.deleteAll();

        mockMvc.perform(post("/api/review/user/createReview").contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(dtoOk))
                        .principal(principal)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Review for film testFilm has been added"));

        mockMvc.perform(get("/api/review/public/getAllReviews"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].title").value("testFilm"))
                .andExpect(jsonPath("$.[0].reviews.[0].text").value("test-text-2"))
                .andExpect(jsonPath("$.[0].reviews.[0].score").value(5));
    }

    @Test
    @DisplayName("create review with no matching film present, should return status 404 and message")
    public void createReviewFilmNotPresent() throws Exception {
        mockMvc.perform(post("/api/review/user/createReview").contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(dtoError))
                .principal(principal)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message")
                        .value("Cannot find the film named " + dtoError.getFilmTitle()));
    }
}
