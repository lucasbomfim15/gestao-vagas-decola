package com.example.demo.modules.company.controllers;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.demo.modules.company.entity.JobEntity;
import com.example.demo.modules.company.repository.CompanyRepository;
import com.example.demo.modules.company.repository.JobRepository;
import com.example.demo.modules.company.entity.CompanyEntity;
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
public class ListCompanyJobsControllerTest {

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
        // Cria uma empresa fake
        CompanyEntity company = new CompanyEntity();
        company.setName("Empresa Teste");
        company.setEmail("empresa@email.com");
        company.setPassword("123456"); // sem encoding se não estiver usando BCrypt
        companyRepository.save(company);
        companyId = company.getId();

        // Cria uma vaga fake
        JobEntity job = new JobEntity();
        job.setDescription("Desenvolvedor Java");
        job.setBenefits("Plano de saúde, Vale refeição");
        job.setLevel("Pleno");
        job.setCompanyId(companyId);
        jobRepository.save(job);

        // Gera token da empresa
        token = TestUtils.generateToken(companyId, TestUtils.COMPANY_SECRET);
    }

    @Test
    void should_return_company_jobs_list_when_authenticated() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/company/job/")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray()) // Verifica se é uma lista
                .andExpect(jsonPath("$[0].description").value("Desenvolvedor Java"));
    }


    @Test
    void should_return_400_when_company_id_is_missing_in_token() throws Exception {
        // Gera um token inválido (sem o claim correto de company_id)
        // Simulamos isso passando um token com subject nulo ou inválido
        String invalidToken = JWT.create()
                .withIssuer("javagas")
                .withClaim("roles", List.of("COMPANY"))
                .sign(Algorithm.HMAC256(TestUtils.COMPANY_SECRET));

        mockMvc.perform(MockMvcRequestBuilders.get("/company/job/")
                        .header("Authorization", "Bearer " + invalidToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
