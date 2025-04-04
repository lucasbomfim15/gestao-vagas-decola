package com.example.demo.modules.company.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateJobRequestDTO {

    @NotBlank
    @Schema(example = "Vacancy for junior developer", requiredMode = Schema.RequiredMode.REQUIRED)
    private String description;

    @Schema(example = "GYM PASS, HEALTH PLAN", requiredMode = Schema.RequiredMode.REQUIRED)
    private String benefits;

    @NotBlank
    @Schema(example = "JUNIOR", requiredMode = Schema.RequiredMode.REQUIRED)
    private String level;


}