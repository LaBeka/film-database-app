package com.edu.film_database.controller.filmController;

import com.edu.film_database.model.Film;
import com.edu.film_database.repo.FilmRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class GetFilmByIdTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    FilmRepository repo;


    private Film film;
    private int filmId;

    @BeforeEach
    public void setUp(){
        repo.deleteAll();
        film = new Film();
        film.setTitle("testFilm");
        film.setReleaseYear(1901);
        film.setGenre("test");
        film.setAspectRatio("4:3");
        filmId = repo.save(film).getId();
    }

    @Test
    @DisplayName("getFilm with one matching film, should return status 200 and the film")
    public void getFilmById() throws Exception{
        mockMvc.perform(get("/film/id/1"))
                .andExpect(status().isOk())
                .andExpect((ResultMatcher) jsonPath("$.filmId").value(filmId))
                .andExpect((ResultMatcher) jsonPath("$.title").value("testFilm"))
                .andExpect((ResultMatcher) jsonPath("$.releaseYear").value(1901))
                .andExpect((ResultMatcher) jsonPath("$.genre").value("test"))
                .andExpect((ResultMatcher) jsonPath("$.aspectRatio").value("4:3"));
    }



}
