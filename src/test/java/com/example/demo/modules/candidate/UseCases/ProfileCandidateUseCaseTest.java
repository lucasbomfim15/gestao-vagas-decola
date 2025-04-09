package com.example.demo.modules.candidate.UseCases;

import com.example.demo.modules.candidate.dtos.ProfileCanididateResponseDTO;
import com.example.demo.modules.candidate.entity.CandidateEntity;
import com.example.demo.modules.candidate.exceptions.UserNotFoundException;
import com.example.demo.modules.candidate.repository.CandidateRepository;
import com.example.demo.modules.candidate.useCases.ProfileCandidateUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProfileCandidateUseCaseTest {

    @Mock
    private CandidateRepository candidateRepository;

    @InjectMocks
    private ProfileCandidateUseCase profileCandidateUseCase;

    private UUID candidateId;
    private CandidateEntity mockCandidate;

    @BeforeEach
    void setUp() {
        candidateId = UUID.randomUUID();
        mockCandidate = new CandidateEntity();
        mockCandidate.setId(candidateId);
        mockCandidate.setName("John Doe");
        mockCandidate.setEmail("john@example.com");
        mockCandidate.setUsername("johndoe");
        mockCandidate.setDescription("Experienced developer");
    }

    @Test
    void execute_WhenCandidateExists_ShouldReturnProfileDTO() {
        // Arrange
        when(candidateRepository.findById(candidateId)).thenReturn(Optional.of(mockCandidate));

        // Act
        ProfileCanididateResponseDTO result = profileCandidateUseCase.execute(candidateId);

        // Assert
        assertNotNull(result);
        assertEquals(candidateId, result.getId());
        assertEquals("John Doe", result.getName());
        assertEquals("john@example.com", result.getEmail());
        assertEquals("johndoe", result.getUsername());
        assertEquals("Experienced developer", result.getDescription());

        verify(candidateRepository, times(1)).findById(candidateId);
    }

    @Test
    void execute_WhenCandidateNotExists_ShouldThrowUserNotFoundException() {
        // Arrange
        when(candidateRepository.findById(candidateId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> {
            profileCandidateUseCase.execute(candidateId);
        });

        verify(candidateRepository, times(1)).findById(candidateId);
    }
}