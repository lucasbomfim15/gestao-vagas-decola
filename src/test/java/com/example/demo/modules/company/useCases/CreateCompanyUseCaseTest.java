package com.example.demo.modules.company.useCases;

import com.example.demo.modules.candidate.exceptions.UserFoundException;
import com.example.demo.modules.company.dto.CreateCompanyRequestDTO;
import com.example.demo.modules.company.dto.CreateCompanyResponseDTO;
import com.example.demo.modules.company.entity.CompanyEntity;
import com.example.demo.modules.company.mappers.CompanyMapper;
import com.example.demo.modules.company.repository.CompanyRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CreateCompanyUseCaseTest {

    @InjectMocks
    private CreateCompanyUseCase createCompanyUseCase;

    @Mock
    private CompanyRepository companyRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    public void shouldCreateCompanySuccessfully() {
        // Arrange
        CreateCompanyRequestDTO requestDTO = CreateCompanyRequestDTO.builder()
                .username("javagasCompany")
                .email("javagasCompany@mail.com")
                .password("1234567890")
                .name("Java Gás")
                .description("Descrição")
                .website("https://javagas.com")
                .build();

        when(companyRepository.findByEmailOrUsername(any(), any()))
                .thenReturn(Optional.empty());

        when(passwordEncoder.encode("1234567890"))
                .thenReturn("encrypted123");

        CompanyEntity savedEntity = CompanyMapper.toEntity(requestDTO, "encrypted123");
        when(companyRepository.save(any()))
                .thenReturn(savedEntity);

        // Act
        CreateCompanyResponseDTO result = createCompanyUseCase.execute(requestDTO);

        // Assert
        assertEquals("javagasCompany", result.getUsername());
        assertEquals("javagasCompany@mail.com", result.getEmail());
        verify(companyRepository).save(any());
        verify(passwordEncoder).encode("1234567890");
    }

    @Test
    public void shouldThrowUserFoundExceptionWhenCompanyAlreadyExists() {
        CreateCompanyRequestDTO requestDTO = CreateCompanyRequestDTO.builder()
                .username("javagasCompany")
                .email("javagasCompany@mail.com")
                .password("1234567890")
                .build();

        when(companyRepository.findByEmailOrUsername("javagasCompany@mail.com", "javagasCompany"))
                .thenReturn(Optional.of(new CompanyEntity()));

        assertThrows(UserFoundException.class, () -> createCompanyUseCase.execute(requestDTO));

        verify(companyRepository, never()).save(any());
        verify(passwordEncoder, never()).encode(any());
    }

}
