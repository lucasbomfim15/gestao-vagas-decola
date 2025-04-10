package com.example.demo.modules.company.useCases;

import com.example.demo.modules.candidate.exceptions.CompanyNotFoundException;
import com.example.demo.modules.company.entity.CompanyEntity;
import com.example.demo.modules.company.entity.JobEntity;
import com.example.demo.modules.company.repository.CompanyRepository;
import com.example.demo.modules.company.repository.JobRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CreateJobUseCaseTest {

    @InjectMocks
    private CreateJobUseCase createJobUseCase;


    @Mock
    private CompanyRepository companyRepository;

    @Mock
    private JobRepository jobRepository;


    @Test
    public void shouldCreateJobSuccessfully() {
        UUID companyId = UUID.randomUUID();

        JobEntity job = JobEntity.builder()
                .description("Desenvolvimento backend")
                .benefits("Vale alimentação, Plano de saúde")
                .level("Pleno")
                .companyId(companyId)
                .build();

        when(companyRepository.findById(companyId)).thenReturn(Optional.of(new CompanyEntity()));
        when(jobRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        JobEntity result = createJobUseCase.execute(job);

        assertEquals("Desenvolvimento backend", result.getDescription());
        assertEquals("Vale alimentação, Plano de saúde", result.getBenefits());
        assertEquals("Pleno", result.getLevel());
        assertEquals(companyId, result.getCompanyId());

        verify(companyRepository).findById(companyId);
        verify(jobRepository).save(job);
    }

    @Test
    public void shouldThrowCompanyNotFoundExceptionWhenCompanyDoesNotExist() {
        UUID companyId = UUID.randomUUID();

        JobEntity job = JobEntity.builder()
                .description("Desenvolvimento backend")
                .benefits("Vale alimentação")
                .level("Júnior")
                .companyId(companyId)
                .build();

        when(companyRepository.findById(companyId)).thenReturn(Optional.empty());

        assertThrows(CompanyNotFoundException.class, () -> createJobUseCase.execute(job));

        verify(companyRepository).findById(companyId);
        verify(jobRepository, never()).save(any());
    }

}
