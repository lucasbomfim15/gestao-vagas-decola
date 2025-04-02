package com.example.demo.modules.candidate.useCases;

import com.example.demo.modules.candidate.entity.CandidateEntity;
import com.example.demo.modules.candidate.exceptions.UserFoundException;
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

    public CandidateEntity execute(CandidateEntity candidate) {
        this.candidateRepository.findByEmailOrUsername(candidate.getEmail(), candidate.getUsername()).ifPresent((user) -> {
            throw new UserFoundException();
        });

        var password =  passwordEncoder.encode(candidate.getPassword());

        candidate.setPassword(password);

        return this.candidateRepository.save(candidate);

    }
}
