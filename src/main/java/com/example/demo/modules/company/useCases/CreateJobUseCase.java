package com.example.demo.modules.company.useCases;

import com.example.demo.modules.candidate.exceptions.CompanyNotFoundException;
import com.example.demo.modules.company.entity.JobEntity;
import com.example.demo.modules.company.repository.CompanyRepository;
import com.example.demo.modules.company.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreateJobUseCase {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private CompanyRepository companyRepository;

    public JobEntity execute(JobEntity jobEntity) {

        companyRepository.findById(jobEntity.getCompanyId()).orElseThrow(() -> {
           throw new CompanyNotFoundException();
        });

       return this.jobRepository.save(jobEntity);
    }
}
