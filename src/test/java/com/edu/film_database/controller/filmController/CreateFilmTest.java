package com.edu.film_database.controller.filmController;


import com.edu.film_database.dto.request.FilmRequestDTO;
import com.edu.film_database.model.Film;
import com.edu.film_database.repo.FilmRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class CreateFilmTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FilmRepository repo;

    private FilmRequestDTO DTO;

    private Film film;
    private int filmId;

    @BeforeEach
    public void setUp() {
        repo.deleteAll();

        film = new Film();
        film.setTitle("testFilm");
        film.setReleaseYear(1901);
        film.setGenre("test");
        film.setAspectRatio("4:3");
        filmId = repo.save(film).getId();

        DTO = new FilmRequestDTO();
        DTO.setTitle("testFilm");
        DTO.setReleaseYear(1901);
        DTO.setGenre("test");


    }

    @Test
    @DisplayName("Create film´, should be successful")
    public void createFilm() throws Exception {
        mockMvc.perform( post("/film/create").contentType(MediaType.APPLICATION_JSON)
                         .content(new ObjectMapper().writeValueAsString(DTO))
                         .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.title").value("testFilm"))
        .andExpect(jsonPath("$.releaseYear").value(1901))
        .andExpect(jsonPath("$.genre").value("test"));

    }

}
