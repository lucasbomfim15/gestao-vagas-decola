package com.example.demo.modules.candidate.UseCases;

import com.example.demo.modules.candidate.useCases.ListAllJobsByFilterUseCase;
import com.example.demo.modules.company.entity.JobEntity;
import com.example.demo.modules.company.repository.JobRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ListAllJobsByFilterUseCaseTest {

    @InjectMocks
    private ListAllJobsByFilterUseCase useCase;

    @Mock
    private JobRepository jobRepository;

    @Test
    public void shouldReturnJobsMatchingFilter() {
        var job1 = new JobEntity();
        job1.setDescription("Java Developer");

        var job2 = new JobEntity();
        job2.setDescription("Senior Java Engineer");

        String filter = "java";

        when(jobRepository.findByDescriptionContainingIgnoreCase(filter))
                .thenReturn(List.of(job1, job2));

        var result = useCase.execute(filter);

        assertEquals(2, result.size());
        assertTrue(result.get(0).getDescription().toLowerCase().contains(filter));
        assertTrue(result.get(1).getDescription().toLowerCase().contains(filter));

        verify(jobRepository).findByDescriptionContainingIgnoreCase(filter);
    }
}