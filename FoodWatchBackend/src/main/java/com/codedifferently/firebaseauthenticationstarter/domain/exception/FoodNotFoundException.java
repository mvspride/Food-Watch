package com.codedifferently.firebaseauthenticationstarter.domain.exception;

public class FoodNotFoundException extends RuntimeException{
    public FoodNotFoundException(String filename) {
        super(filename+ " not found");
    }
}
