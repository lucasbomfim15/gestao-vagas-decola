package com.example.demo.modules.candidate.useCases;

import com.example.demo.modules.candidate.dtos.UpdateCandidateDTO;
import com.example.demo.modules.candidate.entity.CandidateEntity;
import com.example.demo.modules.candidate.exceptions.UserNotFoundException;
import com.example.demo.modules.candidate.repository.CandidateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UpdateCandidateUseCase {

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public CandidateEntity execute(UpdateCandidateDTO dto) {
        // Obtendo o usuário autenticado corretamente
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        System.out.println("Username autenticado: " + principal);

        // Buscar candidato autenticado
        CandidateEntity candidate = candidateRepository.findByUsername(dto.getUsername())
                .orElseThrow(() -> {
                    System.out.println("Candidato não encontrado no banco com username: " + principal);
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

        return candidateRepository.save(candidate);
    }
}