package com.example.demo.modules.company.useCases;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.demo.modules.candidate.exceptions.UserFoundException;
import com.example.demo.modules.company.dto.AuthCompanyDTO;
import com.example.demo.modules.company.dto.AuthCompanyResponseDTO;
import com.example.demo.modules.company.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;

@Service
public class AuthCompanyUseCase {

    @Value("${security.token.secret}")
    private String secretKey;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    public AuthCompanyResponseDTO execute(AuthCompanyDTO authCompanyDTO) throws RuntimeException{
        var company = this.companyRepository.findByUsername(authCompanyDTO.getUsername()).orElseThrow(
                () -> {
                    throw new UsernameNotFoundException("Company not found");
                }
        );

        Boolean passwordMatches = this.passwordEncoder.matches(authCompanyDTO.getPassword(), company.getPassword());

        if (!passwordMatches) {
            throw new RuntimeException("Invalid credentials");  // Alterado para RuntimeException
        }

        Algorithm algorithm = Algorithm.HMAC256(secretKey);

        var expiresin = Instant.now().plus(Duration.ofHours(2));

        var token = JWT.create().withIssuer("javagas")
                .withExpiresAt(expiresin)
                .withSubject(company.getId().toString()).withClaim("roles", Arrays.asList("COMPANY")).sign(algorithm);

        var authCompanyResponseDto = AuthCompanyResponseDTO.builder().acess_token(token).expires_in(expiresin.toEpochMilli()).build();

        return authCompanyResponseDto;
    }
}
