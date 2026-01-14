package com.edu.film_database.exception;

public class FilmNotFoundException extends RuntimeException{

    public FilmNotFoundException(String message){
        super(message);
    }
}
