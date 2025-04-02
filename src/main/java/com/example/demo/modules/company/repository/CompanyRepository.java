package com.example.demo.modules.company.repository;

import com.example.demo.modules.candidate.entity.CandidateEntity;
import com.example.demo.modules.company.entity.CompanyEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface CompanyRepository extends CrudRepository<CompanyEntity, UUID> {
    Optional<CompanyEntity> findByEmailOrUsername(String email, String username);
    Optional<CompanyEntity> findByUsername(String username);
}
