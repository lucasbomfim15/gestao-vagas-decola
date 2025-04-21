package com.example.demo.modules.candidate.mappers;

import com.example.demo.modules.candidate.dtos.CreateCandidateRequestDTO;
import com.example.demo.modules.candidate.dtos.CreateCandidateResponseDTO;
import com.example.demo.modules.candidate.dtos.UpdateCandidateResponseDTO;
import com.example.demo.modules.candidate.entity.CandidateEntity;

public class CandidateMapper {


    public static CandidateEntity toEntity(CreateCandidateRequestDTO dto, String encodedPassword) {
        return CandidateEntity.builder()
                .name(dto.getName())
                .username(dto.getUsername())
                .email(dto.getEmail())
                .password(encodedPassword)
                .phone(dto.getPhone())
                .curriculum(dto.getCurriculum())
                .description(dto.getDescription())
                .build();
    }

    public static CreateCandidateResponseDTO toResponseDTO(CandidateEntity entity) {
        return CreateCandidateResponseDTO.builder()
                .name(entity.getName())
                .username(entity.getUsername())
                .email(entity.getEmail())
                .phone(entity.getPhone())
                .curriculum(entity.getCurriculum())
                .description(entity.getDescription())
                .build();
    }


    public static UpdateCandidateResponseDTO updateToResponseDTO(CandidateEntity entity) {
        return UpdateCandidateResponseDTO.builder()
                .name(entity.getName())
                .username(entity.getUsername())
                .email(entity.getEmail())
                .phone(entity.getPhone())
                .curriculum(entity.getCurriculum())
                .description(entity.getDescription())
                .build();
    }
}
