package com.example.demo.modules.candidate.controllers;

import com.example.demo.modules.candidate.dtos.ProfileCanididateResponseDTO;
import com.example.demo.modules.candidate.entity.CandidateEntity;
import com.example.demo.modules.candidate.exceptions.UserFoundException;
import com.example.demo.modules.candidate.repository.CandidateRepository;
import com.example.demo.modules.candidate.useCases.CreateCandidateUseCase;
import com.example.demo.modules.candidate.useCases.ListAllJobsByFilterUseCase;
import com.example.demo.modules.candidate.useCases.ProfileCandidateUseCase;
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


    @PostMapping("/")
    @PreAuthorize("hasRole('candidate')")
    @Operation(summary = "Candidate Registration", description = "this function is responsible for register user ")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(
                            schema =  @Schema(implementation = CandidateEntity.class)
                    )
            }),
            @ApiResponse(responseCode = "400", description = "User already exists:")
    })
    public ResponseEntity<Object> create(@Valid @RequestBody CandidateEntity candidate) throws Exception {
        try{
            var result =  this.createCandidateUseCase.execute(candidate);
            return ResponseEntity.ok().body(result);
        } catch(Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

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
       try{
           var profile = this.profileCandidateUseCase.execute(UUID.fromString(idCandidate.toString()));
           return ResponseEntity.ok().body(profile);
       } catch(Exception e){
           return ResponseEntity.badRequest().body(e.getMessage());
       }
    }

    @GetMapping("/job")
    @PreAuthorize("hasRole('candidate')")
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




}
