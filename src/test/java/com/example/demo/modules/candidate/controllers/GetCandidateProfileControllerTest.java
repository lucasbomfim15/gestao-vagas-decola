package com.example.demo.modules.candidate.controllers;

import com.example.demo.modules.candidate.entity.CandidateEntity;
import com.example.demo.modules.candidate.repository.CandidateRepository;
import com.example.demo.utils.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.UUID;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class GetCandidateProfileControllerTest {

    private MockMvc mvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private CandidateRepository candidateRepository;

    private static final String CANDIDATE_SECRET = "JAVAGAS_@123#CANDIDATE";

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }

    @Test
    public void should_return_candidate_profile_when_authenticated() throws Exception {
        // Cria e salva um candidato no banco
        var candidate = CandidateEntity.builder()
                .email("lucas@email.com")
                .username("lucasb")
                .password("123456")
                .name("Lucas Bomfim")
                .build();
        candidate = candidateRepository.save(candidate);

        // Gera token com o ID do candidato e secret correto
        var token = TestUtils.generateCandidateToken(candidate.getId(), CANDIDATE_SECRET);

        var result = mvc.perform(
                MockMvcRequestBuilders.get("/candidate/")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk());

        System.out.println(result.andReturn().getResponse().getContentAsString());
    }

    @Test
    public void should_return_bad_request_if_candidate_not_found() throws Exception {
        var token = TestUtils.generateCandidateToken(UUID.randomUUID(), CANDIDATE_SECRET);

        mvc.perform(
                MockMvcRequestBuilders.get("/candidate/")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}
