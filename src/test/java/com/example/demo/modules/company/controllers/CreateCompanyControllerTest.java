package com.example.demo.modules.company.controllers;

import com.example.demo.modules.company.entity.CompanyEntity;
import com.example.demo.modules.company.repository.CompanyRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.context.TestPropertySource;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {
        "security.token.secret.company=JAVAGAS_@123#COMPANY"
})
public class CreateCompanyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        companyRepository.deleteAll();
    }

    @Test
    void shouldCreateCompanySuccessfully() throws Exception {
        CompanyEntity company = new CompanyEntity();
        company.setName("TechCorp");
        company.setEmail("tech@corp.com");
        company.setUsername("techcorp");
        company.setPassword("12345678");


        mockMvc.perform(post("/company/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(company)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("TechCorp"))
                .andExpect(jsonPath("$.email").value("tech@corp.com"))
                .andExpect(jsonPath("$.username").value("techcorp"));
    }

    @Test
    void shouldNotCreateCompanyIfEmailOrUsernameAlreadyExists() throws Exception {
        // Inserindo previamente uma empresa
        CompanyEntity existingCompany = new CompanyEntity();
        existingCompany.setName("OldCorp");
        existingCompany.setEmail("old@corp.com");
        existingCompany.setUsername("oldcorp");
        existingCompany.setPassword(passwordEncoder.encode("senha123"));
        companyRepository.save(existingCompany);

        // Tentando cadastrar outra com o mesmo email
        CompanyEntity newCompany = new CompanyEntity();
        newCompany.setName("NewCorp");
        newCompany.setEmail("old@corp.com"); // email duplicado
        newCompany.setUsername("newcorp");
        newCompany.setPassword("senha123");

        mockMvc.perform(post("/company/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newCompany)))
                .andExpect(status().isBadRequest());
    }
}
