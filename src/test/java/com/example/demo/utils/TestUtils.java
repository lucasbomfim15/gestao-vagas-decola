package com.example.demo.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.UUID;

public class TestUtils {
    private static final ObjectMapper mapper = new ObjectMapper();


    static {
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    public static String objectToJson(Object object) throws JsonProcessingException {
        return mapper.writeValueAsString(object);
    }

    public static String generateToken(UUID idCompany, String secret ) {
        Algorithm algorithm = Algorithm.HMAC256(secret);

        var expiresin = Instant.now().plus(Duration.ofHours(2));

        var token = JWT.create().withIssuer("javagas")
                .withExpiresAt(expiresin)
                .withSubject(idCompany.toString()).withClaim("roles", Arrays.asList("COMPANY")).sign(algorithm);
        return token;
    }

    public static String generateCandidateToken(UUID idCandidate, String secret) {
        Algorithm algorithm = Algorithm.HMAC256(secret);

        var expiresIn = Instant.now().plus(Duration.ofHours(2));

        return JWT.create()
                .withIssuer("javagas")
                .withExpiresAt(expiresIn)
                .withSubject(idCandidate.toString())
                .withClaim("roles", Arrays.asList("CANDIDATE")) // <- importante: minúsculo!
                .sign(algorithm);
    }

}
