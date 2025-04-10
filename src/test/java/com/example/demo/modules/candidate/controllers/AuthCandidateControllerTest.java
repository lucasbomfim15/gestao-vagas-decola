package com.example.demo.modules.candidate.controllers;

import com.example.demo.modules.candidate.dtos.AuthCandidateRequestDTO;
import com.example.demo.modules.candidate.dtos.AuthCandidateResponseDTO;
import com.example.demo.modules.candidate.useCases.AuthCandidateUseCase;
import com.example.demo.providers.JWTCandidateProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {
        "security.token.secret.candidate=JAVAGAS_@123#CANDIDATE"
})
class AuthCandidateControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthCandidateUseCase authCandidateUseCase;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldAuthenticateCandidate() throws Exception {
        // Arrange
        var request = new AuthCandidateRequestDTO("joao", "senha123");
        var response = AuthCandidateResponseDTO.builder()
                .acess_token("fake-token-jwt")
                .expires_in(999999999L)
                .build();

        Mockito.when(authCandidateUseCase.execute(any())).thenReturn(response);

        // Act + Assert
        mockMvc.perform(post("/candidate/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.acess_token").value("fake-token-jwt"))
                .andExpect(jsonPath("$.expires_in").value(999999999L));
    }
}
