package com.example.demo.modules.candidate.UseCases;

import com.example.demo.modules.candidate.entity.AplyJobEntity;
import com.example.demo.modules.candidate.entity.CandidateEntity;
import com.example.demo.modules.candidate.exceptions.JobNotFoundException;
import com.example.demo.modules.candidate.exceptions.UserFoundException;
import com.example.demo.modules.candidate.exceptions.UserNotFoundException;
import com.example.demo.modules.candidate.repository.AplyJobRepository;
import com.example.demo.modules.candidate.repository.CandidateRepository;
import com.example.demo.modules.candidate.useCases.AplyJobCandidateUseCase;
import com.example.demo.modules.company.entity.JobEntity;
import com.example.demo.modules.company.repository.JobRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AplyJobCandidateUseCaseTest {

    @InjectMocks
    private AplyJobCandidateUseCase aplyJobCandidateUseCase;

    @Mock
    private CandidateRepository candidateRepository;

    @Mock
    private JobRepository jobRepository;

    @Mock
    private AplyJobRepository applyJobRepository;

    @Test
    @DisplayName("Should not be able to apply job with candidate not found")
    public void should_not_be_able_to_apply_job_with_candidate_not_found(){
        UUID candidateId = UUID.randomUUID();
        UUID jobId = UUID.randomUUID();

        when(candidateRepository.findById(candidateId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            aplyJobCandidateUseCase.execute(candidateId, jobId);
        });

    }

    @Test
    public void should_not_be_able_to_apply_job_with_job_not_found(){
        UUID candidateId = UUID.randomUUID();

        var candidateEntity = new CandidateEntity();
        candidateEntity.setId(UUID.randomUUID());


        when(candidateRepository.findById(candidateId)).thenReturn(Optional.of(candidateEntity));


        assertThrows(JobNotFoundException.class, () -> {
            aplyJobCandidateUseCase.execute(candidateId, null);
        });


    }

    @Test
    public void should_be_able_to_create_a_new_apply_job() {
        var idCandidate = UUID.randomUUID();
        var idJob = UUID.randomUUID();

        var applyJob = AplyJobEntity.builder().candidateId(idCandidate)
                .jobId(idJob).build();

        var applyJobCreated = AplyJobEntity.builder().id(UUID.randomUUID()).build();

        when(candidateRepository.findById(idCandidate)).thenReturn(Optional.of(new CandidateEntity()));
        when(jobRepository.findById(idJob)).thenReturn(Optional.of(new JobEntity()));

        when(applyJobRepository.save(applyJob)).thenReturn(applyJobCreated);

        var result = aplyJobCandidateUseCase.execute(idCandidate, idJob);

        assertThat(result).hasFieldOrProperty("id");
        assertNotNull(result.getId());

    }
}
