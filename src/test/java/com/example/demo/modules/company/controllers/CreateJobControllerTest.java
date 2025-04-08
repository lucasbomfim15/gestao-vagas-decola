package com.example.demo.modules.company.controllers;

import com.example.demo.modules.candidate.exceptions.CompanyNotFoundException;
import com.example.demo.modules.company.dto.CreateJobRequestDTO;
import com.example.demo.modules.company.entity.CompanyEntity;
import com.example.demo.modules.company.repository.CompanyRepository;
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

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class CreateJobControllerTest {

    private MockMvc mvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private CompanyRepository companyRepository;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }

    @Test
    public void should_be_able_to_create_a_new_job() throws Exception {
        var company = CompanyEntity.builder()
                .description("COMPANY_DESCRIPTION")
                .email("company@email.com") // Email válido!
                .password("1234567890")
                .username("COMPANY_USERNAME")
                .name("COMPANY_NAME")
                .build();
        company = companyRepository.save(company);

        var createdJobDto = CreateJobRequestDTO.builder()
                .benefits("benefits_test")
                .description("description_test")
                .level("level_test")
                .build();

        var result = mvc.perform(
                MockMvcRequestBuilders.post("/company/job/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.objectToJson(createdJobDto))
                        .header("Authorization", TestUtils.generateToken(company.getId(), "JAVAGAS_@123#"))
        ).andExpect(MockMvcResultMatchers.status().isOk());

        System.out.println(result.andReturn().getResponse().getContentAsString());
    }

    @Test
    public void should_not_be_able_to_create_a_new_job_if_the_company_does_not_found() throws Exception {
        var createdJobDto = CreateJobRequestDTO.builder()
                .benefits("benefits_test")
                .description("description_test")
                .level("level_test")
                .build();

        mvc.perform(
                MockMvcRequestBuilders.post("/company/job/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.objectToJson(createdJobDto))
                        .header("Authorization", TestUtils.generateToken(UUID.randomUUID(), "JAVAGAS_@123#"))
        ).andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}
