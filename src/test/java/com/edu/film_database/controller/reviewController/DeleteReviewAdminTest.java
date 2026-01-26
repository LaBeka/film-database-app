package com.edu.film_database.controller.reviewController;

import com.edu.film_database.dto.request.UpdateReviewRequestDto;
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

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class DeleteReviewAdminTest {

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
    private Role role_other;
    private User user;
    private User otherUser;
    private Film film;
    private Review review;
    private Principal principal;
    private Principal principal_other;
    private UpdateReviewRequestDto dtoOk;
    private int film_tmp;

    @BeforeEach
    public void setUp() {
        review_repo.deleteAll();
        film_repo.deleteAll();
        user_repo.deleteAll();
        role_repo.deleteAll();

        role = new Role();
        role.setName("ADMIN");
        role_repo.save(role);

        role_other = new Role();
        role_other.setName("USER");
        role_repo.save(role_other);

        user = new User();
        user.setUsername("testUser");
        user.setEmail("testUser@somedomain.com");
        user.setPassword("test123");
        user.setFullName("testUserFullName");
        user.setAge(23);
        user.setCurrentlyActive(true);
        user.setRoles(Set.of(role));
        user_repo.save(user);

        otherUser = new User();
        otherUser.setUsername("testUser2");
        otherUser.setEmail("testUser2@somedomain.com");
        otherUser.setPassword("test321");
        otherUser.setFullName("testUser2FullName");
        otherUser.setAge(25);
        otherUser.setCurrentlyActive(true);
        otherUser.setRoles(Set.of(role_other));
        user_repo.save(otherUser);

        Authentication auth =
                new UsernamePasswordAuthenticationToken(
                        "testUser", null, List.of(new SimpleGrantedAuthority("ROLE_ADMIN"))
                );

        SecurityContextHolder.getContext().setAuthentication(auth);

        film = new Film();
        film.setTitle("testFilm");
        film.setAgeRestriction(15);
        film.setAspectRatio("22:10");
        film_tmp = film_repo.save(film).getId();

        review = new Review();
        review.setText("test-text");
        review.setDate(LocalDate.now());
        review.setScore(5);
        review.setUser(otherUser);
        review.setFilm(film);
        review_repo.save(review);

        principal = new UserPrincipal("testUser@somedomain.com");
        principal_other = new UserPrincipal("testUser2@somedomain.com");

        dtoOk = new UpdateReviewRequestDto(1,3, "test-text.update");
    }

    @Test
    @DisplayName("DeleteReviewAdmin with review not by admin present, " +
            "should return status 200 and message")
    public void deleteReviewAdminPresent() throws Exception {
        mockMvc.perform(get("/api/review/public/getAllReviews"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].filmId").value(film_tmp))
                .andExpect(jsonPath("$.[0].reviews.[0].text").value("test-text"))
                .andExpect(jsonPath("$.[0].reviews.[0].score").value(5));

        mockMvc.perform(delete("/api/review/admin/deleteReview/" +
                        (review_repo.findAll().get(0).getId()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .principal(principal)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.filmId").value(film_tmp))
                .andExpect(jsonPath("$.reviews.[0].text").value("test-text"))
                .andExpect(jsonPath("$.reviews.[0].score").value(5));

        mockMvc.perform(get("/api/review/public/getAllReviews"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].filmId").value(film_tmp))
                .andExpect(jsonPath("$.[0].reviews").isEmpty());
    }

    @Test
    @DisplayName("DeleteReviewAdmin with no review present, " +
            "should return status 404 and message")
    public void deleteReviewAdminNotPresent() throws Exception {
        review_repo.deleteAll();

        mockMvc.perform(delete("/api/review/admin/deleteReview/" + (film_tmp + 1))
                        .contentType(MediaType.APPLICATION_JSON)
                        .principal(principal)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message")
                        .value("Cannot delete a review " +
                                "that does not exist"));
    }
}
