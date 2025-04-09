package com.example.demo.modules.candidate.controllers;

import com.example.demo.modules.candidate.entity.AplyJobEntity;
import com.example.demo.modules.candidate.entity.CandidateEntity;
import com.example.demo.modules.candidate.repository.AplyJobRepository;
import com.example.demo.modules.candidate.repository.CandidateRepository;
import com.example.demo.modules.company.entity.JobEntity;
import com.example.demo.modules.company.repository.JobRepository;
import com.example.demo.utils.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ApplyJobControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private AplyJobRepository aplyJobRepository;

    private CandidateEntity testCandidate;
    private JobEntity testJob;
    private String validToken;

    @BeforeEach
    void setup() {
        aplyJobRepository.deleteAll();
        jobRepository.deleteAll();
        candidateRepository.deleteAll();

        testCandidate = new CandidateEntity();
        testCandidate.setName("Test Candidate");
        testCandidate.setUsername("testcandidate");
        testCandidate.setEmail("test@candidate.com");
        testCandidate.setPassword("123456");
        testCandidate.setDescription("Test description");
        testCandidate = candidateRepository.save(testCandidate);

        testJob = new JobEntity();
        testJob.setDescription("Test Job Description");
        testJob.setBenefits("Test Benefits");
        testJob.setLevel("Junior");
        testJob = jobRepository.save(testJob);

        validToken = TestUtils.generateCandidateToken(testCandidate.getId(), "JAVAGAS_@123#CANDIDATE");
    }

    @Test
    void applyJob_WithValidData_ShouldReturnCreated() throws Exception {
        String requestBody = String.format("{\"jobId\": \"%s\"}", testJob.getId());

        mockMvc.perform(post("/candidate/job/apply")
                        .header("Authorization", "Bearer " + validToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk());

        // Verificação atualizada
        List<AplyJobEntity> applications = aplyJobRepository.findByCandidateId(testCandidate.getId());
        assertFalse(applications.isEmpty());
        assertEquals(testJob.getId(), applications.get(0).getJobId());
    }

    @Test
    void applyJob_WithoutAuthorization_ShouldReturnUnauthorized() throws Exception {
        String requestBody = String.format("{\"jobId\": \"%s\"}", testJob.getId());

        mockMvc.perform(post("/candidate/job/apply")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void applyJob_WithInvalidJobId_ShouldReturnBadRequest() throws Exception {
        UUID invalidJobId = UUID.randomUUID();
        String requestBody = String.format("{\"jobId\": \"%s\"}", invalidJobId);

        MvcResult result = mockMvc.perform(post("/candidate/job/apply")
                        .header("Authorization", "Bearer " + validToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andReturn();

        assertTrue(result.getResponse().getContentAsString().contains("Job not found"));
    }

//    @Test
//    void applyJob_ToSameJobTwice_ShouldAllowAndCreateNewApplication() throws Exception {
//        String requestBody = String.format("{\"jobId\": \"%s\"}", testJob.getId());
//
//        // Primeira aplicação
//        mockMvc.perform(post("/candidate/job/apply")
//                        .header("Authorization", "Bearer " + validToken)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(requestBody))
//                .andExpect(status().isOk());
//
//        // Segunda aplicação
//        mockMvc.perform(post("/candidate/job/apply")
//                        .header("Authorization", "Bearer " + validToken)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(requestBody))
//                .andExpect(status().isOk());
//
//        // Verificação atualizada
//        List<AplyJobEntity> applications = aplyJobRepository.findByCandidateIdAndJobId(testCandidate.getId(), testJob.getId());
//        assertEquals(2, applications.size());
//    }
}