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
    @SpringBootTest
    @AutoConfigureMockMvc(addFilters = false)
    public class FindByIdTest {

        @Autowired
        FilmRepository repo;



        private int ID = 1;

        @BeforeEach
        public void setUp(){
            repo.deleteAll();

            Film film = new Film();
            film.setTitle("testFilm");
            film.setReleaseYear(1901);
            film.setGenre("test");
            repo.save(film);

        }

        @Test
        @DisplayName("Find film by id, should return one film")
        public void findByIdPresent() throws Exception{
            Optional<Film> op = repo.findById(ID);
            if(op.isPresent()){
                Film filmFromRepo = op.get();
                assertEquals("testFilm" , filmFromRepo.getTitle());
                assertEquals(1901,  filmFromRepo.getReleaseYear());
                assertEquals("test" , filmFromRepo.getGenre());
            }
            else {throw new Exception(); }

        }

        @Test
        @DisplayName("Find film by id, should returns no films")
        public void findByIdEmpty() throws Exception {
            repo.deleteAll();
            Optional<Film> op = repo.findById(ID);
            if (op.isEmpty()){
                // ALL GOOD
            }
            else {
             throw new Exception();
            }

        }

    }