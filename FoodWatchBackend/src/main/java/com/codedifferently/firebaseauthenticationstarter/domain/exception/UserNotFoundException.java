package com.codedifferently.firebaseauthenticationstarter.domain.exception;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(String email) {
        super(email + " not found");
    }
}