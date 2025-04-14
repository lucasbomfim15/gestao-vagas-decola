package com.example.demo.modules.company.controllers;


import com.example.demo.modules.company.dto.CreateJobRequestDTO;
import com.example.demo.modules.company.entity.JobEntity;
import com.example.demo.modules.company.repository.CompanyRepository;
import com.example.demo.modules.company.useCases.CreateJobUseCase;
import com.example.demo.modules.company.useCases.ListCompanyJobsUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/company/job")
@Tag(name = "Vacancies Company", description = "view all vacancies created by the company\n")
public class JobController {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private CreateJobUseCase createJobUseCase;

    @Autowired
    private ListCompanyJobsUseCase listCompanyJobsUseCase;

    @PostMapping("/")
    @PreAuthorize("hasRole('COMPANY')")
    @Tag(name = "Vacancies", description = "Vacancies information")
    @Operation(summary = "Job registration", description = "This function is responsible for registering vacancies within the company")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = JobEntity.class))
            }),
            @ApiResponse(responseCode = "400", description = "Invalid input or Company ID not found/invalid")
    })
    @SecurityRequirement(name = "jwt_auth")
    public ResponseEntity<Object> createJob(@Valid @RequestBody CreateJobRequestDTO jobRequest, HttpServletRequest request) {

        String companyId = (String) request.getAttribute("company_id");

        if (companyId == null || companyId.isEmpty()) {
            throw new IllegalArgumentException("Company ID not found in token");
        }

            UUID companyUUID = UUID.fromString(companyId);

            JobEntity jobEntity = new JobEntity();
            jobEntity.setDescription(jobRequest.getDescription());
            jobEntity.setBenefits(jobRequest.getBenefits());
            jobEntity.setLevel(jobRequest.getLevel());
            jobEntity.setCompanyId(companyUUID);

            var result = this.createJobUseCase.execute(jobEntity);
           return ResponseEntity.ok().body(result);

    }


    @GetMapping("/")
    @PreAuthorize("hasRole('COMPANY')")
    @Operation(summary = "List company's jobs", description = "Returns a list of all jobs created by the authenticated company")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Jobs found", content = {
                    @Content(array = @ArraySchema(schema = @Schema(implementation = JobEntity.class)))
            }),
            @ApiResponse(responseCode = "400", description = "Company ID not found in token")
    })
    @SecurityRequirement(name = "jwt_auth")
    public ResponseEntity<List<JobEntity>> listCompanyJobs(HttpServletRequest request) {
        String companyId = (String) request.getAttribute("company_id");

        if (companyId == null || companyId.isEmpty()) {
            throw new IllegalArgumentException("Company ID not found in token");
        }

        UUID companyUUID = UUID.fromString(companyId);
        List<JobEntity> jobs = listCompanyJobsUseCase.execute(companyUUID);

        return ResponseEntity.ok(jobs);
    }
}
