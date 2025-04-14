package com.example.demo.modules.candidate.UseCases;

import com.example.demo.modules.candidate.dtos.UpdateCandidateDTO;
import com.example.demo.modules.candidate.entity.CandidateEntity;
import com.example.demo.modules.candidate.exceptions.UserNotFoundException;
import com.example.demo.modules.candidate.repository.CandidateRepository;
import com.example.demo.modules.candidate.useCases.UpdateCandidateUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UpdateCandidateUseCaseTest {

    @Mock
    private CandidateRepository candidateRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UpdateCandidateUseCase updateCandidateUseCase;

    private UUID mockedCandidateId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Mockando contexto de segurança
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);

        mockedCandidateId = UUID.randomUUID();

        when(authentication.getPrincipal()).thenReturn(mockedCandidateId.toString());
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void shouldUpdateCandidateSuccessfully() {
        var dto = new UpdateCandidateDTO();
        dto.setName("Novo Nome");
        dto.setEmail("novo@email.com");
        dto.setPhone("999999999");
        dto.setCurriculum("Novo currículo");
        dto.setDescription("Nova descrição");
        dto.setPassword("novaSenha");

        CandidateEntity candidate = new CandidateEntity();
        candidate.setId(mockedCandidateId); // <-- usa o mesmo ID aqui

        when(candidateRepository.findById(mockedCandidateId)).thenReturn(Optional.of(candidate));
        when(passwordEncoder.encode("novaSenha")).thenReturn("senhaCriptografada");
        when(candidateRepository.save(any())).thenReturn(candidate);

        var result = updateCandidateUseCase.execute(dto);

        assertEquals("Novo Nome", result.getName());
        assertEquals("novo@email.com", result.getEmail());
        assertEquals("999999999", result.getPhone());
        assertEquals("Novo currículo", result.getCurriculum());
        assertEquals("Nova descrição", result.getDescription());
        assertEquals("senhaCriptografada", result.getPassword());

        verify(candidateRepository).save(candidate);
    }

    @Test
    void shouldThrowUserNotFoundExceptionWhenCandidateNotExists() {
        var dto = new UpdateCandidateDTO();
        when(candidateRepository.findById(mockedCandidateId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> updateCandidateUseCase.execute(dto));
    }
}
