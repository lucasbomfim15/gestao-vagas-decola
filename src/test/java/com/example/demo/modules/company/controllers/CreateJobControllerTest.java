package com.example.demo.modules.company.controllers;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.demo.modules.company.entity.CompanyEntity;
import com.example.demo.modules.company.repository.CompanyRepository;
import com.example.demo.modules.company.repository.JobRepository;
import com.example.demo.utils.TestUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
public class CreateJobControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private CompanyRepository companyRepository;

    private String token;
    private UUID companyId;

    @BeforeAll
    void setUp() {
        CompanyEntity company = new CompanyEntity();
        company.setName("Empresa Criadora");
        company.setEmail("criadora@email.com");
        company.setPassword("123456");
        companyRepository.save(company);
        companyId = company.getId();

        token = TestUtils.generateToken(companyId, TestUtils.COMPANY_SECRET);
    }

    @Test
    void should_create_job_when_authenticated_and_valid_data() throws Exception {
        String requestBody = """
            {
              "description": "Backend Developer",
              "level": "Sênior",
              "benefits": "Vale transporte, Home office"
            }
        """;

        mockMvc.perform(MockMvcRequestBuilders.post("/company/job/")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("Backend Developer"))
                .andExpect(jsonPath("$.level").value("Sênior"))
                .andExpect(jsonPath("$.benefits").value("Vale transporte, Home office"));
    }

    @Test
    void should_return_400_when_token_does_not_contain_company_id() throws Exception {
        String requestBody = """
            {
              "description": "Frontend Developer",
              "level": "Júnior",
              "benefits": "Vale alimentação"
            }
        """;

        String invalidToken = JWT.create()
                .withIssuer("javagas")
                .withClaim("roles", List.of("COMPANY"))
                .sign(Algorithm.HMAC256(TestUtils.COMPANY_SECRET));

        mockMvc.perform(MockMvcRequestBuilders.post("/company/job/")
                        .header("Authorization", "Bearer " + invalidToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());
    }
}
