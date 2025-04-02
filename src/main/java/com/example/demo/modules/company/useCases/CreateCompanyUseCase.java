package com.example.demo.modules.company.useCases;

import com.example.demo.modules.candidate.exceptions.UserFoundException;
import com.example.demo.modules.company.entity.CompanyEntity;
import com.example.demo.modules.company.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CreateCompanyUseCase {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public CompanyEntity execute(CompanyEntity companyEntity) {
        this.companyRepository.findByEmailOrUsername(companyEntity.getEmail(), companyEntity.getUsername()).ifPresent((company) -> {
                throw new UserFoundException();
        });

        var password = passwordEncoder.encode(companyEntity.getPassword());
        companyEntity.setPassword(password);

        return this.companyRepository.save(companyEntity);
    }
}
