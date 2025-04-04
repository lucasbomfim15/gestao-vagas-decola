package com.example.demo.modules.company.controllers;


import com.example.demo.modules.candidate.dtos.AuthCandidateResponseDTO;
import com.example.demo.modules.company.dto.AuthCompanyDTO;
import com.example.demo.modules.company.dto.AuthCompanyResponseDTO;
import com.example.demo.modules.company.useCases.AuthCompanyUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;

@RestController
@RequestMapping("/company")
@CrossOrigin(origins = "*")
@Tag(name = "Company Authentication", description = "Company Authentication information")
public class AuthCompanyController {

    @Autowired
    private AuthCompanyUseCase authCompanyUseCase;

    @PostMapping("/auth")
    @Operation(summary = "Company Authentication", description = "this function is responsible for authenticantion company ")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(
                            schema =  @Schema(implementation = AuthCompanyResponseDTO.class)
                    )
            }),
            @ApiResponse(responseCode = "400", description = "Company not found")
    })
    public ResponseEntity<?> create(@RequestBody AuthCompanyDTO authCompanyDTO) {
        try {
            var token = this.authCompanyUseCase.execute(authCompanyDTO);
            return ResponseEntity.ok(token);
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }


}
