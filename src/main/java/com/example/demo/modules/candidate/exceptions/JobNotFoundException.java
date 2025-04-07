package com.example.demo.modules.candidate.exceptions;

public class JobNotFoundException  extends RuntimeException{
    public JobNotFoundException(){
        super("Job not found");
    }
}
