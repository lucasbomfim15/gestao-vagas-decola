package com.example.demo.modules.candidate.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateCandidateRequestDTO {

    private String name;
    private String username;
    private String email;
    private String password;
    private String phone;
    private String curriculum;
    private String description;

}
