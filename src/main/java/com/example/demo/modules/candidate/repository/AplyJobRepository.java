package com.example.demo.modules.candidate.repository;

import com.example.demo.modules.candidate.entity.AplyJobEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AplyJobRepository extends JpaRepository<AplyJobEntity, UUID> {
    List<AplyJobEntity> findByCandidateId(UUID candidateId);
    List<AplyJobEntity> findByJobId(UUID jobId);
    boolean existsByCandidateIdAndJobId(UUID candidateId, UUID jobId);
}
