package com.example.demo.modules.candidate.useCases;

import com.example.demo.modules.candidate.dtos.UpdateCandidateDTO;
import com.example.demo.modules.candidate.dtos.UpdateCandidateRequestDTO;
import com.example.demo.modules.candidate.dtos.UpdateCandidateResponseDTO;
import com.example.demo.modules.candidate.entity.CandidateEntity;
import com.example.demo.modules.candidate.exceptions.UserNotFoundException;
import com.example.demo.modules.candidate.mappers.CandidateMapper;
import com.example.demo.modules.candidate.repository.CandidateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UpdateCandidateUseCase {

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UpdateCandidateResponseDTO execute(UpdateCandidateRequestDTO dto) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // Se o principal for String (como no teste), ele é o ID
        String candidateId = (String) principal;

        CandidateEntity candidate = candidateRepository.findById(UUID.fromString(candidateId))
                .orElseThrow(() -> {
                    System.out.println("Candidato não encontrado no banco com id: " + candidateId);
                    return new UserNotFoundException();
                });

        // Atualizar apenas os campos não nulos
        if (dto.getName() != null) {
            candidate.setName(dto.getName());
        }
        if (dto.getEmail() != null) {
            candidate.setEmail(dto.getEmail());
        }
        if (dto.getPhone() != null) {
            candidate.setPhone(dto.getPhone());
        }
        if (dto.getCurriculum() != null) {
            candidate.setCurriculum(dto.getCurriculum());
        }
        if (dto.getDescription() != null) {
            candidate.setDescription(dto.getDescription());
        }
        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            candidate.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        return CandidateMapper.updateToResponseDTO(candidate);
    }
}