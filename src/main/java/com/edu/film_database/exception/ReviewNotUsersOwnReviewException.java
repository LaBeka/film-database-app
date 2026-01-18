package com.edu.film_database.exception;

public class ReviewNotUsersOwnReviewException extends RuntimeException{

    public ReviewNotUsersOwnReviewException(String message){
        super(message);
    }
}
