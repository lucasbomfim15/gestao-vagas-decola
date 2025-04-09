package com.example.demo.modules.candidate.UseCases;

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

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
        var candidate = new CandidateEntity();
        candidate.setUsername("mateus");
        candidate.setEmail("mateus@mail.com");
        candidate.setPassword("123");

        when(repository.findByEmailOrUsername(any(), any())).thenReturn(Optional.empty());
        when(encoder.encode("123")).thenReturn("encrypted123");
        when(repository.save(any())).thenReturn(candidate);

        var result = useCase.execute(candidate);

        assertEquals("mateus", result.getUsername());
        verify(encoder).encode("123");
        verify(repository).save(candidate);
    }

    @Test
    public void shouldThrowUserFoundExceptionWhenCandidateAlreadyExists() {
        var existingCandidate = new CandidateEntity();
        existingCandidate.setUsername("mateus");
        existingCandidate.setEmail("mateus@mail.com");

        var candidate = new CandidateEntity();
        candidate.setUsername("mateus");
        candidate.setEmail("mateus@mail.com");
        candidate.setPassword("123");

        when(repository.findByEmailOrUsername("mateus@mail.com", "mateus"))
                .thenReturn(Optional.of(existingCandidate));

        assertThrows(UserFoundException.class, () -> useCase.execute(candidate));

        verify(repository, never()).save(any()); // não deve tentar salvar
        verify(encoder, never()).encode(any()); // nem codificar senha
    }

    @Test
    public void shouldEncryptPasswordBeforeSavingCandidate() {
        var candidate = new CandidateEntity();
        candidate.setUsername("mateus");
        candidate.setEmail("mateus@mail.com");
        candidate.setPassword("123");

        when(repository.findByEmailOrUsername("mateus@mail.com", "mateus"))
                .thenReturn(Optional.empty());
        when(encoder.encode("123")).thenReturn("encrypted123");
        when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0)); // retorna o que foi salvo

        var result = useCase.execute(candidate);

        assertEquals("encrypted123", result.getPassword());
        verify(encoder).encode("123");
        verify(repository).save(candidate);
    }

}