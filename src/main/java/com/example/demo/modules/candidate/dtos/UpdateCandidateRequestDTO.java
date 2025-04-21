package com.example.demo.modules.candidate.dtos;

import lombok.Data;

@Data
public class UpdateCandidateRequestDTO {

    private String name;
    private String username;
    private String email;
    private String phone;
    private String curriculum;
    private String description;
    private String password;
}
