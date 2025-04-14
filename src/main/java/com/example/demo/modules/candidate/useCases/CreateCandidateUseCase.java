package com.example.demo.modules.candidate.useCases;

import com.example.demo.modules.candidate.dtos.CreateCandidateRequestDTO;
import com.example.demo.modules.candidate.dtos.CreateCandidateResponseDTO;
import com.example.demo.modules.candidate.entity.CandidateEntity;
import com.example.demo.modules.candidate.exceptions.UserFoundException;
import com.example.demo.modules.candidate.mappers.CandidateMapper;
import com.example.demo.modules.candidate.repository.CandidateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CreateCandidateUseCase {




    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public CreateCandidateResponseDTO execute(CreateCandidateRequestDTO createCandidateRequestDTO) {
        this.candidateRepository.findByEmailOrUsername(createCandidateRequestDTO.getEmail(), createCandidateRequestDTO.getUsername()).ifPresent((user) -> {
            throw new UserFoundException();
        });

        var password =  passwordEncoder.encode(createCandidateRequestDTO.getPassword());

        var candidate = CandidateMapper.toEntity(createCandidateRequestDTO, password);

        var savedCandidate = this.candidateRepository.save(candidate);

        return CandidateMapper.toResponseDTO(savedCandidate);


    }
}
