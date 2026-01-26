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

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class GetFilmByIdTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FilmRepository repo;


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

        mockMvc.perform(get("/api/film/id/" + filmId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(filmId))
                .andExpect(jsonPath("$.title").value("testFilm"))
                .andExpect(jsonPath("$.releaseYear").value(1901))
                .andExpect(jsonPath("$.genre").value("test"))
                .andExpect(jsonPath("$.aspectRatio").value("4:3"));
    }



}
