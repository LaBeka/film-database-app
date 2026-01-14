package com.edu.film_database.exception;

public class ReviewNotFoundException extends RuntimeException{

    public ReviewNotFoundException(String message){
        super(message);
    }
}
