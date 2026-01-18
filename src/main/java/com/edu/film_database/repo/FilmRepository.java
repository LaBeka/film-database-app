package com.edu.film_database.repo;

import com.edu.film_database.model.Film;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FilmRepository extends JpaRepository<Film, Integer> {

    Optional<Film> findByTitle(String title);
}
