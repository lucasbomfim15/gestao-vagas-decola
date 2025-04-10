package com.example.demo.modules.company.useCases;

import com.example.demo.modules.candidate.exceptions.UserFoundException;
import com.example.demo.modules.company.entity.CompanyEntity;
import com.example.demo.modules.company.repository.CompanyRepository;
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
public class CreateCompanyUseCaseTest {

    @InjectMocks
    private CreateCompanyUseCase createCompanyUseCase;

    @Mock
    private CompanyRepository companyRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    public void shoulCreateCompanySuccessfully() {
        CompanyEntity company = new CompanyEntity();

        company.setUsername("javagasCompany");
        company.setEmail("javagasCompany@mail.com");
        company.setPassword("1234567890");

        when(companyRepository.findByEmailOrUsername(any(), any())).thenReturn(Optional.empty());
        when(passwordEncoder.encode("1234567890")).thenReturn("encrypted123");
        when(companyRepository.save(any())).thenReturn(company);

        var result = createCompanyUseCase.execute(company);

        assertEquals("javagasCompany", result.getUsername());
        verify(passwordEncoder).encode("1234567890");
        verify(companyRepository).save(company);
    }

    @Test
    public void shouldThrowUserFoundExceptionWhenCompanyAlreadyExists() {
        CompanyEntity company = new CompanyEntity();
        company.setUsername("javagasCompany");
        company.setEmail("javagasCompany@mail.com");
        company.setPassword("1234567890");

        when(companyRepository.findByEmailOrUsername("javagasCompany@mail.com", "javagasCompany"))
                .thenReturn(Optional.of(new CompanyEntity()));

        assertThrows(UserFoundException.class, () -> createCompanyUseCase.execute(company));

        verify(companyRepository, never()).save(any());
        verify(passwordEncoder, never()).encode(any());
    }

    @Test
    public void shouldEncryptPasswordBeforeSaving() {
        CompanyEntity company = new CompanyEntity();
        company.setUsername("javagasCompany");
        company.setEmail("javagasCompany@mail.com");
        company.setPassword("plainPassword");

        when(companyRepository.findByEmailOrUsername(any(), any())).thenReturn(Optional.empty());
        when(passwordEncoder.encode("plainPassword")).thenReturn("hashedPassword");
        when(companyRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        CompanyEntity result = createCompanyUseCase.execute(company);

        assertEquals("hashedPassword", result.getPassword());
        verify(passwordEncoder).encode("plainPassword");
        verify(companyRepository).save(company);
    }

    @Test
    public void shouldReturnSavedCompany() {
        CompanyEntity company = new CompanyEntity();
        company.setUsername("javagasCompany");
        company.setEmail("javagasCompany@mail.com");
        company.setPassword("1234567890");

        when(companyRepository.findByEmailOrUsername(any(), any())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(any())).thenReturn("encodedPassword");
        when(companyRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        CompanyEntity result = createCompanyUseCase.execute(company);

        assertEquals(company.getUsername(), result.getUsername());
        assertEquals(company.getEmail(), result.getEmail());
        assertEquals("encodedPassword", result.getPassword());
    }

}
