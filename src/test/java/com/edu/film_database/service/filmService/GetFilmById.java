package com.edu.film_database.service.filmService;


import com.edu.film_database.dto.response.FilmResponseDTO;
import com.edu.film_database.model.Film;
import com.edu.film_database.repo.FilmRepository;
import com.edu.film_database.service.FilmService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GetFilmById {

    @Mock
    private FilmRepository repo;

    @Spy
    @InjectMocks
    FilmService service;

    private Film film;
    private int ID;
    private FilmResponseDTO DTO;

    @BeforeEach
    public void setUp(){
        film = new Film();
        film.setTitle("testFilm");

        DTO = new FilmResponseDTO();
        DTO.setId(ID);
        DTO.setTitle("testFilm");

    }

    @Test
    @DisplayName("Film from FindFilmByID(), should return film")
    public void getFilmById(){

        when(repo.getById(ID)).thenReturn(film);

        FilmResponseDTO fromService = service.findById(ID);

        assertEquals(fromService, DTO);
    }

}
