package com.example.demo.modules.candidate.entity;

import com.example.demo.modules.company.entity.JobEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Table(name = "apply_jobs")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class AplyJobEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "candidate_id", insertable = false, updatable = false)
    private CandidateEntity candidateEntity;

    @ManyToOne
    @JoinColumn(name = "job_id", insertable = false, updatable = false)
    private JobEntity jobEntity;

    @Column(name = "candidate_id")
    private UUID candidateId;


    @Column(name = "job_id")
    private UUID jobId;


    @CreationTimestamp
    private LocalDateTime createdAt;
}
