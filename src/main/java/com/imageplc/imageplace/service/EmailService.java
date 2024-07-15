package com.imageplc.imageplace.service;

import com.imageplc.imageplace.entity.VerificationCodeEntity;
import com.imageplc.imageplace.repository.VerificationCodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class EmailService {
    private final Random random = new Random();
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private VerificationCodeRepository verificationCodeRepository;

    public void sendVerificationCode(String toEmail) {
        var code = generateVerificationCode();
        saveVerificationCode(toEmail, code);
        sendRegisterEmail(toEmail, code);
    }

    public void sendLoginVerificationCode(String toEmail) {
        var code = generateVerificationCode();
        saveVerificationCode(toEmail, code);
        sendLoginEmail(toEmail, code);
    }

    private String generateVerificationCode() {
        return String.format("%06d", random.nextInt(1000000));
    }

    private void saveVerificationCode(String email, String code) {
        verificationCodeRepository.save(new VerificationCodeEntity(email, code));
    }

    private void sendRegisterEmail(String toEmail, String code) {
        var message = new SimpleMailMessage();
        message.setFrom("2218723143@qq.com");
        message.setTo(toEmail);
        message.setSubject("Verification Code");
        message.setText("【PLC图床】验证码：" + code + "（5分钟内有效）。您正在注册PLC图床，请勿将验证码告诉他人哦。");
        mailSender.send(message);
    }

    private void sendLoginEmail(String toEmail, String code) {
        var message = new SimpleMailMessage();
        message.setFrom("2218723143@qq.com");
        message.setTo(toEmail);
        message.setSubject("Verification Code");
        message.setText("【PLC图床】验证码：" + code + "（5分钟内有效）。您正在以管理员身份登录PLC图床，请勿将验证码告诉他人哦。");
        mailSender.send(message);
    }
}
