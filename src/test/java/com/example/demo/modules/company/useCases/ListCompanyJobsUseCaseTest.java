package com.example.demo.modules.company.useCases;

import com.example.demo.modules.company.entity.JobEntity;
import com.example.demo.modules.company.repository.JobRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ListCompanyJobsUseCaseTest {

    private JobRepository jobRepository;
    private ListCompanyJobsUseCase listCompanyJobsUseCase;

    @BeforeEach
    void setUp() {
        jobRepository = Mockito.mock(JobRepository.class);
        listCompanyJobsUseCase = new ListCompanyJobsUseCase(jobRepository);
    }

    @Test
    void shouldReturnListOfJobsForGivenCompanyId() {
        // Arrange
        UUID companyId = UUID.randomUUID();

        JobEntity job1 = new JobEntity();
        job1.setId(UUID.randomUUID());
        job1.setDescription("Desenvolvedor Java");

        JobEntity job2 = new JobEntity();
        job2.setId(UUID.randomUUID());
        job2.setDescription("Engenheiro de Software");

        List<JobEntity> mockJobs = Arrays.asList(job1, job2);

        when(jobRepository.findByCompanyId(companyId)).thenReturn(mockJobs);

        // Act
        List<JobEntity> result = listCompanyJobsUseCase.execute(companyId);

        // Assert
        assertEquals(2, result.size());
        assertEquals("Desenvolvedor Java", result.get(0).getDescription());
        verify(jobRepository, times(1)).findByCompanyId(companyId);
    }
}
