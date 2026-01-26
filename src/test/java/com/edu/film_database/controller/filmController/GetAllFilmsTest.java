package com.edu.film_database.controller.filmController;

import com.edu.film_database.model.Film;
import com.edu.film_database.repo.FilmRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class GetAllFilmsTest {


    private MockMvc mockMvc;

    FilmRepository repo;

    private Film film1;
    private Film film2;


    public void setUp(){
        repo.deleteAll();

        film1 = new Film();
        film1.setTitle("test1");
        film1.setReleaseYear(1901);

        film2 = new Film();
        film2.setTitle("test2");
        film2.setReleaseYear(1902);

    }

    @Test
    @DisplayName("get all films, should return status 200 and two films")
    public void getAllFilmsPresent() throws Exception{
        mockMvc.perform(get("/film/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].title").value("test1"))
                .andExpect(jsonPath("$.[0].releaseYear").value(1901))
                .andExpect(jsonPath("$.[1].title").value("test2"))
                .andExpect(jsonPath("$.[1].releaseYear").value(1902));
    }

    @Test
    @DisplayName("get all films, should return status 200 and no films")
    public void getAllFilmsEmpty() throws Exception{
        mockMvc.perform(get("/film/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }







}
