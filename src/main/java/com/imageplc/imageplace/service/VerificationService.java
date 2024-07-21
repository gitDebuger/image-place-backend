package com.imageplc.imageplace.service;

import com.imageplc.imageplace.repository.VerificationCodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class VerificationService {
    @Autowired
    private VerificationCodeRepository verificationCodeRepository;

    public boolean notMatchVerifyCode(String email, String code) {
        var thresholdTime = LocalDateTime.now().minusMinutes(5);
        var entity = verificationCodeRepository.findValidCode(email, code, thresholdTime);
        return entity == null;
    }
}
