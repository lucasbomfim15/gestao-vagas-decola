package com.example.demo.modules.company.controllers;


import com.example.demo.modules.company.entity.JobEntity;
import com.example.demo.modules.company.repository.CompanyRepository;
import com.example.demo.modules.company.useCases.CreateJobUseCase;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/company/job")
public class JobController {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private CreateJobUseCase createJobUseCase;

    @PostMapping("/")
    @PreAuthorize("hasRole('COMPANY')")
    public JobEntity createJob(@Valid @RequestBody JobEntity jobEntity, HttpServletRequest request) {
        // Obtém o company_id como String
        String companyId = (String) request.getAttribute("company_id");



        if (companyId == null || companyId.isEmpty()) {
            throw new IllegalArgumentException("Company ID not found in token");
        }

        try {

            UUID companyUUID = UUID.fromString(companyId);
            jobEntity.setCompanyId(companyUUID);
            return this.createJobUseCase.execute(jobEntity);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid Company ID format: " + companyId);
        }
    }
}
