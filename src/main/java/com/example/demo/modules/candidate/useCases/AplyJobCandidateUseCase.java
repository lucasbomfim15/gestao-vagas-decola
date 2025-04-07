package com.example.demo.modules.candidate.useCases;


import com.example.demo.modules.candidate.entity.AplyJobEntity;
import com.example.demo.modules.candidate.exceptions.JobNotFoundException;
import com.example.demo.modules.candidate.exceptions.UserNotFoundException;
import com.example.demo.modules.candidate.repository.AplyJobRepository;
import com.example.demo.modules.candidate.repository.CandidateRepository;
import com.example.demo.modules.company.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AplyJobCandidateUseCase {

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private AplyJobRepository applyJobRepository;

    public AplyJobEntity execute(UUID idCandidate, UUID idJob){
        this.candidateRepository.findById(idCandidate).orElseThrow(() -> {
            throw new UserNotFoundException();
        });

        this.jobRepository.findById(idJob).orElseThrow(() -> {
            throw new JobNotFoundException();
        });

        var applyJob = AplyJobEntity.builder()
                .candidateId(idCandidate)
                .jobId(idJob).build();

        applyJob = applyJobRepository.save(applyJob);

        return applyJob;

    }

}
