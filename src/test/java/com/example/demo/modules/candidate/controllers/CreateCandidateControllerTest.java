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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class CreateCandidateControllerTest {

    private MockMvc mvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private CandidateRepository candidateRepository;

    @BeforeEach
    public void setup() {
        candidateRepository.deleteAll();

        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }

    @Test
    public void should_be_able_to_create_a_new_candidate() throws Exception {
        var candidate = CandidateEntity.builder()
                .description("description_test")
                .email("candidate@email.com")
                .password("1234567890")
                .username("candidate_username")
                .name("candidate_name")
                .build();

        var result = mvc.perform(
                MockMvcRequestBuilders.post("/candidate/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.objectToJson(candidate))
        ).andExpect(MockMvcResultMatchers.status().isOk());

        System.out.println(result.andReturn().getResponse().getContentAsString());
    }

    @Test
    public void should_not_be_able_to_create_candidate_if_user_already_exists() throws Exception {
        var candidate = CandidateEntity.builder()
                .description("description_test")
                .email("candidate@email.com")
                .password("1234567890")
                .username("candidate_username")
                .name("candidate_name")
                .build();

        // Salva o candidato no banco
        candidateRepository.save(candidate);

        // Tenta criar novamente
        mvc.perform(
                MockMvcRequestBuilders.post("/candidate/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.objectToJson(candidate))
        ).andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}
