package com.example.demo.modules.candidate.UseCases;


import com.example.demo.modules.candidate.dtos.AuthCandidateRequestDTO;
import com.example.demo.modules.candidate.dtos.ProfileCanididateResponseDTO;
import com.example.demo.modules.candidate.entity.CandidateEntity;
import com.example.demo.modules.candidate.exceptions.UserNotFoundException;
import com.example.demo.modules.candidate.repository.CandidateRepository;
import com.example.demo.modules.candidate.useCases.AuthCandidateUseCase;
import com.example.demo.modules.candidate.useCases.ProfileCandidateUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthCandidateUseCaseTest {


    @InjectMocks
    private AuthCandidateUseCase authCandidateUseCase;

    @Mock
    private CandidateRepository candidateRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(authCandidateUseCase, "secretKey", "mysecretkey123");
    }

    @Test
    void shouldAuthenticateSuccessfully() {
        var candidate = new CandidateEntity();
        candidate.setId(UUID.randomUUID());
        candidate.setUsername("lucas");
        candidate.setPassword("hashedPassword");

        var request = new AuthCandidateRequestDTO("lucas", "123");

        when(candidateRepository.findByUsername("lucas")).thenReturn(Optional.of(candidate));
        when(passwordEncoder.matches("123", "hashedPassword")).thenReturn(true);

        var result = authCandidateUseCase.execute(request);

        assertNotNull(result);
        assertNotNull(result.getAcess_token());
        assertTrue(result.getExpires_in() > 0);
    }

    @Test
    void shouldThrowWhenUsernameNotFound() {
        var request = new AuthCandidateRequestDTO("unknown", "123");

        when(candidateRepository.findByUsername("unknown")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> {
            authCandidateUseCase.execute(request);
        });
    }

    @Test
    void shouldThrowWhenPasswordIncorrect() {
        var candidate = new CandidateEntity();
        candidate.setId(UUID.randomUUID());
        candidate.setUsername("lucas");
        candidate.setPassword("hashedPassword");

        var request = new AuthCandidateRequestDTO("lucas", "wrongpass");

        when(candidateRepository.findByUsername("lucas")).thenReturn(Optional.of(candidate));
        when(passwordEncoder.matches("wrongpass", "hashedPassword")).thenReturn(false);

        assertThrows(BadCredentialsException.class, () -> {
            authCandidateUseCase.execute(request);
        });
    }

}
