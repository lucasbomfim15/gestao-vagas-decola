package com.example.demo.modules.candidate.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileCanididateResponseDTO {

    private String description;
    private String username;
    private String email;
    private UUID id;
    private String name;

}
