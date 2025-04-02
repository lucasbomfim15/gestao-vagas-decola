package com.example.demo.modules.company.useCases;

import com.example.demo.modules.company.entity.JobEntity;
import com.example.demo.modules.company.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreateJobUseCase {

    @Autowired
    private JobRepository jobRepository;

    public JobEntity execute(JobEntity jobEntity) {
       return this.jobRepository.save(jobEntity);
    }
}
