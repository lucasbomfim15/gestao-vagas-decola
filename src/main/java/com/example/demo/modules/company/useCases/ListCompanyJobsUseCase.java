package com.example.demo.modules.company.useCases;

import com.example.demo.modules.company.entity.JobEntity;
import com.example.demo.modules.company.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ListCompanyJobsUseCase {

    @Autowired
    private  JobRepository jobRepository;

    public ListCompanyJobsUseCase(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    public List<JobEntity> execute(UUID companyId) {
        return jobRepository.findByCompanyId(companyId);
    }
}