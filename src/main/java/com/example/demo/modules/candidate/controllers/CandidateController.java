package com.example.demo.modules.candidate.controllers;

import com.example.demo.modules.candidate.entity.CandidateEntity;
import com.example.demo.modules.candidate.exceptions.UserFoundException;
import com.example.demo.modules.candidate.repository.CandidateRepository;
import com.example.demo.modules.candidate.useCases.CreateCandidateUseCase;
import com.example.demo.modules.candidate.useCases.ProfileCandidateUseCase;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/candidate")
public class CandidateController {

    @Autowired
    private CreateCandidateUseCase createCandidateUseCase;

    @Autowired
    private ProfileCandidateUseCase profileCandidateUseCase;


    @PostMapping("/")
    @PreAuthorize("hasRole('candidate')")
    public ResponseEntity<Object> create(@Valid @RequestBody CandidateEntity candidate) throws Exception {
        try{
            var result =  this.createCandidateUseCase.execute(candidate);
            return ResponseEntity.ok().body(result);
        } catch(Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @GetMapping("/")
    public ResponseEntity<Object> get(HttpServletRequest req) {

        var idCandidate = req.getAttribute("candidate_id");
       try{
           var profile = this.profileCandidateUseCase.execute(UUID.fromString(idCandidate.toString()));
           return ResponseEntity.ok().body(profile);
       } catch(Exception e){
           return ResponseEntity.badRequest().body(e.getMessage());
       }
    }




}
