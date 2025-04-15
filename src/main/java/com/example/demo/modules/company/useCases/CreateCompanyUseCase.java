package com.example.demo.modules.company.useCases;

import com.example.demo.modules.candidate.exceptions.UserFoundException;
import com.example.demo.modules.company.dto.CreateCompanyRequestDTO;
import com.example.demo.modules.company.dto.CreateCompanyResponseDTO;
import com.example.demo.modules.company.entity.CompanyEntity;
import com.example.demo.modules.company.mappers.CompanyMapper;
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

    public CreateCompanyResponseDTO execute(CreateCompanyRequestDTO createCompanyRequestDTO) {
        this.companyRepository.findByEmailOrUsername(createCompanyRequestDTO.getEmail(), createCompanyRequestDTO.getUsername()).ifPresent((company) -> {
                throw new UserFoundException();
        });

        var password = passwordEncoder.encode(createCompanyRequestDTO.getPassword());

        CompanyEntity companyEntity = CompanyMapper.toEntity(createCompanyRequestDTO, password);

        CompanyEntity savedEntity = companyRepository.save(companyEntity);

        return CompanyMapper.toResponseDTO(savedEntity);
    }
}
