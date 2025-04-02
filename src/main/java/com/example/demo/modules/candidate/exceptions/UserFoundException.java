package com.example.demo.modules.candidate.exceptions;

public class UserFoundException extends RuntimeException{
    public UserFoundException(){
        super("User already exists:");
    }
}
