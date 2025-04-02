package com.example.demo.modules.candidate.useCases;


import com.example.demo.modules.candidate.dtos.ProfileCanididateResponseDTO;
import com.example.demo.modules.candidate.repository.CandidateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ProfileCandidateUseCase {


    @Autowired
    private CandidateRepository candidateRepository;

    public ProfileCanididateResponseDTO execute(UUID idCandidate) {

       var candidate = this.candidateRepository.findById(idCandidate).orElseThrow(() -> new RuntimeException("candidate not found"));
        var candidateDto = ProfileCanididateResponseDTO.builder()
                .description(candidate.getDescription())
                .username(candidate.getUsername())
                .email(candidate.getEmail())
                .name(candidate.getName())
                .id(candidate.getId()).build();

       return candidateDto;
    }
}
