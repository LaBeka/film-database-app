package com.edu.film_database.repository.filmRepository;

import com.edu.film_database.model.Film;
import com.edu.film_database.repo.FilmRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;


    @ActiveProfiles("test")
    @DataJpaTest
    public class FindByIdTest {

        @Autowired
        FilmRepository repo;

        private int ID;
        private Film film;

        @BeforeEach
        public void setUp(){
            repo.deleteAll();
            film = new Film();
            film.setTitle("testFilm");
            film.setReleaseYear(1901);
            film.setGenre("test");
            ID = repo.save(film).getId();
        }

        @Test
        @DisplayName("Find film by id, should return one film")
        public void findByIdPresent() throws Exception{
            Optional<Film> op = repo.findById(ID);
            assertEquals(film, op.get());
        }

        @Test
        @DisplayName("Find film by id, should returns no films")
        public void findByIdEmpty() throws Exception {
            repo.deleteAll();
            Optional<Film> op = repo.findById(ID);
            assertEquals(Optional.empty(), op);
        }
    }