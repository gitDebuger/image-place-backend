package com.imageplc.imageplace.repository;

import com.imageplc.imageplace.entity.VerificationCodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface VerificationCodeRepository extends JpaRepository<VerificationCodeEntity, String> {
    @Query("SELECT vc FROM VerificationCodeEntity vc WHERE vc.email = :email AND vc.verificationCode = :code AND vc.createdAt >= :thresholdTime")
    VerificationCodeEntity findValidCode(@Param("email") String email, @Param("code") String code, @Param("thresholdTime") LocalDateTime thresholdTime);
}
