package com.codedifferently.firebaseauthenticationstarter.domain.exception;

public class WrongImageType extends RuntimeException{

    public WrongImageType() {
        super("Wrong image Type. Upload PNG,Jpeg,jpg");
    }
}
