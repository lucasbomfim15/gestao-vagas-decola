package com.example.demo.modules.candidate.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity(name = "candidate")
public class CandidateEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String name;

    @Pattern(regexp = "\\S+", message = "this field not must be space beetween lines")
    private String username;

    @Length(min = 3, max = 100)
    private String password;

    @Email(message = "this field must be valid email")
    private String email;
    private String phone;
    private String curriculum;
    private String description;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
