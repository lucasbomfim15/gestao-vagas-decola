package com.example.demo.modules.candidate.exceptions;

import org.springframework.context.MessageSource;

public class CompanyNotFoundException extends RuntimeException{
    public CompanyNotFoundException() {
        super("Company not Found");
    }
}
