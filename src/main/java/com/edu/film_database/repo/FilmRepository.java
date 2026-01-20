package com.edu.film_database.repo;

import com.edu.film_database.model.Film;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Collection;
import java.util.List;

@Repository
public interface FilmRepository extends JpaRepository<Film, Integer> {

    /*
    @Query("SELECT f FROM Film f")
    public List<Film> findAll();

    @Query("SELECT f FROM Film f WHERE f.id = :id ")
    public Film findById(@Param("id") int id);

*/

    @Query("SELECT f FROM Film f WHERE f.title = :query")
    List<Film> findByTitle(@Param("query") String sQuery);

    @Query("SELECT f FROM Film f WHERE f.title LIKE :query%")
    List<Film> serachByTitle(@Param("query") String sQuery);

    @NativeQuery("SELECT * FROM films WHERE cast LIKE %:actor%")
    List<Film> searchByActor(@Param("actor") String cast);

    @Query("SELECT f FROM Film f WHERE f.genre LIKE %:genre%")
    List<Film> findByGenre(@Param("genre")String genre);
}