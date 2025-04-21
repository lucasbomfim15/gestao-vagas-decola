package com.example.demo.modules.candidate.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.nio.file.FileStore;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCandidateResponseDTO {

    private UUID id;
    private String name;
    private String username;
    private String email;
    private String phone;
    private String curriculum;
    private String description;


}
