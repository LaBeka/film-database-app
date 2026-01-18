package com.edu.film_database.repo;

import com.edu.film_database.model.Film;
import com.edu.film_database.model.Review;
import com.edu.film_database.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Integer> {

    public List<Review> findByFilm(Film film);
    public List<Review> findByUserAndFilm(User user, Film film);
}
