package com.imageplc.imageplace.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "verification_code")
public class VerificationCodeEntity {
    @Id
    @Column(name = "email", columnDefinition = "VARCHAR(255)")
    private String email;
    @Setter
    @Column(name = "code", columnDefinition = "CHAR(6)")
    private String verificationCode;
    @Setter
    @Column(name = "created_at", columnDefinition = "DATETIME")
    private LocalDateTime createdAt;
    public VerificationCodeEntity() {}
    public VerificationCodeEntity(String email, String verificationCode) {
        this.email = email;
        this.verificationCode = verificationCode;
        this.createdAt = LocalDateTime.now();
    }
}
