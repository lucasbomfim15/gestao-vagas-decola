package com.example.demo.modules.company.controllers;

import com.example.demo.modules.candidate.exceptions.UserFoundException;
import com.example.demo.modules.company.entity.CompanyEntity;
import com.example.demo.modules.company.repository.CompanyRepository;
import com.example.demo.modules.company.useCases.CreateCompanyUseCase;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/company")
public class CompanyController {
    @Autowired
    private CreateCompanyUseCase createCompanyUseCase;

    @PostMapping("")
    public ResponseEntity<Object> createCompany (@Valid  @RequestBody CompanyEntity companyEntity) {
        try{
            var result = this.createCompanyUseCase.execute(companyEntity);
            return ResponseEntity.ok().body(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
