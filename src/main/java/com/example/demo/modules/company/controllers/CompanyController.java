package com.example.demo.modules.company.controllers;

import com.example.demo.modules.candidate.entity.CandidateEntity;
import com.example.demo.modules.candidate.exceptions.UserFoundException;
import com.example.demo.modules.company.dto.CreateCompanyRequestDTO;
import com.example.demo.modules.company.dto.CreateCompanyResponseDTO;
import com.example.demo.modules.company.entity.CompanyEntity;
import com.example.demo.modules.company.repository.CompanyRepository;
import com.example.demo.modules.company.useCases.CreateCompanyUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/company")
@Tag(name = "Company", description = "Company information")
public class CompanyController {
    @Autowired
    private CreateCompanyUseCase createCompanyUseCase;

    @PostMapping("/")
    @Operation(summary = "Company Registration", description = "this function is responsible for register company ")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(
                            schema =  @Schema(implementation = CompanyEntity.class)
                    )
            }),
            @ApiResponse(responseCode = "400", description = "Company already exists:")
    })
    public ResponseEntity<CreateCompanyResponseDTO> createCompany (@Valid  @RequestBody CreateCompanyRequestDTO createCompanyRequestDTO) {

        CreateCompanyResponseDTO responseDTO = this.createCompanyUseCase.execute(createCompanyRequestDTO);
        return ResponseEntity.ok().body(responseDTO);

    }

}
