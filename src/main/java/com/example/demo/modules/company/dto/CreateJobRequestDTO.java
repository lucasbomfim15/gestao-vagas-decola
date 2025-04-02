package com.example.demo.modules.company.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateJobRequestDTO {
    @NotBlank
    private String description;

    private String benefits;

    @NotBlank
    private String level;

    // Getters e Setters
}