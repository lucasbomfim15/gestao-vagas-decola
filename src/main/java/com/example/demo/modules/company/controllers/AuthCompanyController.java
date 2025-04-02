package com.example.demo.modules.company.controllers;


import com.example.demo.modules.company.dto.AuthCompanyDTO;
import com.example.demo.modules.company.useCases.AuthCompanyUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;

@RestController
@RequestMapping("/company")
@CrossOrigin(origins = "*")
public class AuthCompanyController {

    @Autowired
    private AuthCompanyUseCase authCompanyUseCase;

    @PostMapping("/auth")
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

    @GetMapping("/test")
    public String test() {
        return "Auth route is working!";
    }

    @PostMapping("/test-post")
    public String testPost() {
        return "POST request successful!";
    }

}
