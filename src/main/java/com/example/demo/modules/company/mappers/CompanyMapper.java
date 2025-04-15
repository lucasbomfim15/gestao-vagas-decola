package com.example.demo.modules.company.mappers;

import com.example.demo.modules.candidate.dtos.CreateCandidateRequestDTO;
import com.example.demo.modules.candidate.dtos.CreateCandidateResponseDTO;
import com.example.demo.modules.candidate.entity.CandidateEntity;
import com.example.demo.modules.company.dto.CreateCompanyRequestDTO;
import com.example.demo.modules.company.dto.CreateCompanyResponseDTO;
import com.example.demo.modules.company.entity.CompanyEntity;

public class CompanyMapper {

    public static CompanyEntity toEntity(CreateCompanyRequestDTO dto, String encodedPassword) {
        return CompanyEntity.builder()
                .name(dto.getName())
                .username(dto.getUsername())
                .email(dto.getEmail())
                .password(encodedPassword)
                .website(dto.getWebsite())
                .description(dto.getDescription())
                .build();
    }

    public static CreateCompanyResponseDTO toResponseDTO(CompanyEntity entity) {
        return CreateCompanyResponseDTO.builder()
                .name(entity.getName())
                .username(entity.getUsername())
                .email(entity.getEmail())
                .website(entity.getWebsite())
                .description(entity.getDescription())
                .build();
    }
}
