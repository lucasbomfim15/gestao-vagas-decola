package com.example.demo.modules.candidate.UseCases;

import com.example.demo.modules.candidate.dtos.CreateCandidateRequestDTO;
import com.example.demo.modules.candidate.dtos.CreateCandidateResponseDTO;
import com.example.demo.modules.candidate.entity.CandidateEntity;
import com.example.demo.modules.candidate.exceptions.UserFoundException;
import com.example.demo.modules.candidate.repository.CandidateRepository;
import com.example.demo.modules.candidate.useCases.CreateCandidateUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CreateCandidateUseCaseTest {

    @InjectMocks
    private CreateCandidateUseCase useCase;

    @Mock
    private CandidateRepository repository;

    @Mock
    private PasswordEncoder encoder;

    @Test
    public void shouldCreateCandidateSuccessfully() {
        var dto = CreateCandidateRequestDTO.builder()
                .name("Mateus")
                .username("mateus")
                .email("mateus@mail.com")
                .password("123")
                .phone("81999999999")
                .curriculum("curriculum.pdf")
                .description("Backend Developer")
                .build();

        var savedCandidate = CandidateEntity.builder()
                .name(dto.getName())
                .username(dto.getUsername())
                .email(dto.getEmail())
                .password("encrypted123")
                .phone(dto.getPhone())
                .curriculum(dto.getCurriculum())
                .description(dto.getDescription())
                .build();

        when(repository.findByEmailOrUsername(any(), any())).thenReturn(Optional.empty());
        when(encoder.encode("123")).thenReturn("encrypted123");
        when(repository.save(any())).thenReturn(savedCandidate);

        CreateCandidateResponseDTO result = useCase.execute(dto);

        assertEquals("Mateus", result.getName());
        assertEquals("mateus", result.getUsername());
        assertEquals("mateus@mail.com", result.getEmail());
        assertEquals("Backend Developer", result.getDescription());
        assertEquals("curriculum.pdf", result.getCurriculum());
        assertEquals("81999999999", result.getPhone());
    }

    @Test
    public void shouldThrowUserFoundExceptionWhenCandidateAlreadyExists() {
        var dto = CreateCandidateRequestDTO.builder()
                .username("mateus")
                .email("mateus@mail.com")
                .password("123")
                .build();

        var existingCandidate = CandidateEntity.builder()
                .username("mateus")
                .email("mateus@mail.com")
                .build();

        when(repository.findByEmailOrUsername("mateus@mail.com", "mateus"))
                .thenReturn(Optional.of(existingCandidate));

        assertThrows(UserFoundException.class, () -> useCase.execute(dto));

        verify(repository, never()).save(any());
        verify(encoder, never()).encode(any());
    }

    @Test
    public void shouldEncryptPasswordBeforeSavingCandidate() {
        var dto = CreateCandidateRequestDTO.builder()
                .username("mateus")
                .email("mateus@mail.com")
                .password("123")
                .build();

        when(repository.findByEmailOrUsername(any(), any())).thenReturn(Optional.empty());
        when(encoder.encode("123")).thenReturn("encrypted123");

        when(repository.save(any())).thenAnswer(invocation -> {
            CandidateEntity candidate = invocation.getArgument(0);
            assertEquals("encrypted123", candidate.getPassword());
            return candidate;
        });

        CreateCandidateResponseDTO result = useCase.execute(dto);

        assertEquals("mateus", result.getUsername());
        assertEquals("mateus@mail.com", result.getEmail());

        verify(encoder).encode("123");
        verify(repository).save(any());
    }
}
