package com.example.demo.modules.candidate.controllers;

import com.example.demo.modules.candidate.dtos.ApplyJobRequestDTO;
import com.example.demo.modules.candidate.dtos.ProfileCanididateResponseDTO;
import com.example.demo.modules.candidate.dtos.UpdateCandidateDTO;
import com.example.demo.modules.candidate.entity.CandidateEntity;
import com.example.demo.modules.candidate.exceptions.UserFoundException;
import com.example.demo.modules.candidate.repository.CandidateRepository;
import com.example.demo.modules.candidate.useCases.*;
import com.example.demo.modules.company.entity.JobEntity;
import com.example.demo.modules.company.repository.JobRepository;
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
@RequestMapping("/candidate")
@Tag(name = "Candidate", description = "Candidate information")
public class CandidateController {

    @Autowired
    private CreateCandidateUseCase createCandidateUseCase;

    @Autowired
    private ProfileCandidateUseCase profileCandidateUseCase;

    @Autowired
    private ListAllJobsByFilterUseCase listAllJobsByFilterUseCase;

    @Autowired
    private UpdateCandidateUseCase updateCandidateUseCase;

    @Autowired
    private AplyJobCandidateUseCase aplyJobCandidateUseCase;



    @PostMapping("/")
    @Operation(summary = "Candidate Registration", description = "this function is responsible for register user ")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(
                            schema =  @Schema(implementation = CandidateEntity.class)
                    )
            }),
            @ApiResponse(responseCode = "400", description = "User already exists:")
    })
    public ResponseEntity<Object> create(@Valid @RequestBody CandidateEntity candidate) {

            var result =  this.createCandidateUseCase.execute(candidate);
            return ResponseEntity.ok().body(result);


    }

    @GetMapping("/")
    @Operation(summary = "show user profile", description = "this function is responsible for show user profile")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(
                           schema =  @Schema(implementation = ProfileCanididateResponseDTO.class)
                    )
            }),
            @ApiResponse(responseCode = "400", description = "User not found")
    })
    @SecurityRequirement(name = "jwt_auth")
    public ResponseEntity<Object> get(HttpServletRequest req) {

            var idCandidate = req.getAttribute("candidate_id");

           var profile = this.profileCandidateUseCase.execute(UUID.fromString(idCandidate.toString()));
           return ResponseEntity.ok().body(profile);

    }

    @GetMapping("/job")
    @PreAuthorize("hasRole('CANDIDATE')")
    @Operation(summary = "List of vacancies available to the candidate", description = "List of available vacancies that the candidate can apply for\n")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(
                            array =  @ArraySchema(schema = @Schema(implementation = JobEntity.class))
                    )
            })
    })
    @SecurityRequirement(name = "jwt_auth")
    public List<JobEntity> findJobByFilter(@RequestParam String filter) {
        return this.listAllJobsByFilterUseCase.execute(filter);
    }

    @PutMapping("/")
    @PreAuthorize("hasRole('CANDIDATE')")
    @Operation(summary = "Update candidate profile", description = "Allows the authenticated candidate to update their profile.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Profile updated successfully", content = {
                    @Content(schema = @Schema(implementation = CandidateEntity.class))
            }),
            @ApiResponse(responseCode = "400", description = "Invalid data or user not authenticated")
    })
    @SecurityRequirement(name = "jwt_auth")
    public ResponseEntity<Object> update(@Valid @RequestBody UpdateCandidateDTO dto) {

            var result = this.updateCandidateUseCase.execute(dto);
            return ResponseEntity.ok().body(result);

    }
    
    

    @PostMapping("/job/apply")
    @PreAuthorize("hasRole('CANDIDATE')")
    @Operation(summary = "registration of a candidate for a vacancy\n", description = "registration of a candidate for a vacancy\n")
    @SecurityRequirement(name = "jwt_auth")
    public ResponseEntity<Object> applyJob(HttpServletRequest req, @RequestBody ApplyJobRequestDTO body ) {
        UUID idJob = body.getJobId();

        var idCandidate = req.getAttribute("candidate_id");


        var result =  this.aplyJobCandidateUseCase.execute(UUID.fromString(idCandidate.toString()), idJob);
        return ResponseEntity.ok().body(result);



    }




}
