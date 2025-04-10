package com.example.demo.modules.company.useCases;


import com.example.demo.modules.company.dto.AuthCompanyDTO;
import com.example.demo.modules.company.entity.CompanyEntity;
import com.example.demo.modules.company.repository.CompanyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthCompanyUseCaseTest {

    @InjectMocks
    private AuthCompanyUseCase authCompanyUseCase;

    @Mock
    private CompanyRepository companyRepository;

    @Mock
    PasswordEncoder passwordEncoder;


    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(authCompanyUseCase, "secretKey", "mysecretkey123");
    }

    @Test
    void shouldAuthenticateSuccessfully() {
        var company = new CompanyEntity();
        company.setId(UUID.randomUUID());
        company.setUsername("javagascompany");
        company.setPassword("hashedPassword");

        var request = new AuthCompanyDTO("1234567890", "javagascompany");

        when(companyRepository.findByUsername("javagascompany")).thenReturn(Optional.of(company));
        when(passwordEncoder.matches("1234567890", "hashedPassword")).thenReturn(true);

        var result = authCompanyUseCase.execute(request);

        assertNotNull(result);
        assertNotNull(result.getAcess_token());
        assertTrue(result.getExpires_in() > 0);
    }

}
