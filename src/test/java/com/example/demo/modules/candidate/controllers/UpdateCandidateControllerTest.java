package com.example.demo.modules.candidate.controllers;

import com.example.demo.modules.candidate.dtos.UpdateCandidateDTO;
import com.example.demo.modules.candidate.entity.CandidateEntity;
import com.example.demo.modules.candidate.repository.CandidateRepository;
import com.example.demo.utils.TestUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {
        "security.token.secret.candidate=JAVAGAS_@123#CANDIDATE"
})
public class UpdateCandidateControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    private UUID candidateId;
    private String token;

    @BeforeEach
    void setup() {
        candidateRepository.deleteAll();

        CandidateEntity candidate = new CandidateEntity();
        candidate.setName("João");
        candidate.setEmail("joao@email.com");
        candidate.setUsername("joao123");
        candidate.setPassword(passwordEncoder.encode("123456"));

        candidate = candidateRepository.save(candidate);
        candidateId = candidate.getId();

        token = TestUtils.generateCandidateToken(candidateId, "JAVAGAS_@123#CANDIDATE");
    }

    @Test
    void should_be_able_to_update_candidate_profile() throws Exception {
        UpdateCandidateDTO dto = new UpdateCandidateDTO();
        dto.setName("João Atualizado");
        dto.setEmail("joao.novo@email.com");
        dto.setPassword("nova_senha");

        mvc.perform(put("/candidate/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
                        .header("Authorization", "Bearer " + token)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("João Atualizado")))
                .andExpect(jsonPath("$.email", is("joao.novo@email.com")));
    }
}
