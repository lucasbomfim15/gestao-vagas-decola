package com.example.demo.modules.company.controllers;

import com.example.demo.modules.company.dto.AuthCompanyDTO;
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
        "security.token.secret=JAVAGAS_@123#COMPANY"
})
public class AuthCompanyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        companyRepository.deleteAll();

        CompanyEntity company = new CompanyEntity();
        company.setName("Empresa Teste");
        company.setEmail("empresa@email.com");
        company.setUsername("empresauser");
        company.setPassword(passwordEncoder.encode("senhaSegura123"));

        companyRepository.save(company);
    }

    @Test
    void shouldAuthenticateCompanyAndReturnToken() throws Exception {
        AuthCompanyDTO authDTO = new AuthCompanyDTO();
        authDTO.setUsername("empresauser");
        authDTO.setPassword("senhaSegura123");

        mockMvc.perform(post("/company/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.acess_token").exists())
                .andExpect(jsonPath("$.expires_in").exists());
    }

    @Test
    void shouldReturn400WhenPasswordIsIncorrect() throws Exception {
        AuthCompanyDTO authDTO = new AuthCompanyDTO();
        authDTO.setUsername("empresauser");
        authDTO.setPassword("senhaErrada");

        mockMvc.perform(post("/company/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authDTO)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldReturn400WhenUsernameDoesNotExist() throws Exception {
        AuthCompanyDTO authDTO = new AuthCompanyDTO();
        authDTO.setUsername("usuarioInexistente");
        authDTO.setPassword("qualquercoisa");

        mockMvc.perform(post("/company/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authDTO)))
                .andExpect(status().isUnauthorized());
    }
}
