package com.example.demo.modules.candidate.controllers;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.demo.modules.company.entity.JobEntity;
import com.example.demo.modules.company.repository.JobRepository;
import com.example.demo.modules.candidate.entity.CandidateEntity;
import com.example.demo.modules.candidate.repository.CandidateRepository;
import com.example.demo.utils.TestUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FindJobByFilterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CandidateRepository candidateRepository;

    private String token;

    @BeforeAll
    void setUp() {
        // Cria um candidato fake no banco para teste
        CandidateEntity candidate = new CandidateEntity();
        candidate.setName("Lucas Teste");
        candidate.setEmail("lucas@email.com");
        candidate.setPassword("123456"); // sem encoding se você não usar BCrypt no login
        candidateRepository.save(candidate);

        // Gera o token JWT para esse candidato com a secret correta
        Algorithm algorithm = Algorithm.HMAC256("JAVAGAS_@123#CANDIDATE");

        token = JWT.create()
                .withSubject(candidate.getId().toString())
                .withClaim("roles", List.of("CANDIDATE")) // <- ADICIONA ESSA LINHA
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 10)) // 10 min
                .sign(algorithm);
    }

    @Test
    void should_return_list_of_jobs_when_filter_matches() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/candidate/job") // ajusta o path real do seu endpoint
                        .header("Authorization", "Bearer " + token)
                        .param("filter", "Desenvolvedor") // se tiver filtros
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray()); // ou qualquer outra validação que preferir


    }
}