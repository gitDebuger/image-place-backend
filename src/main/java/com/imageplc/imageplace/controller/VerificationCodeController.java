package com.imageplc.imageplace.controller;

import com.imageplc.imageplace.service.AdminService;
import com.imageplc.imageplace.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class VerificationCodeController {
    @Autowired
    private EmailService emailService;
    @Autowired
    private AdminService adminService;

    @PostMapping("/send-verification-code")
    public ResponseEntity<Map<String, String>> sendVerificationCode(@RequestBody Map<String, String> request) {
        var email = request.get("email");
        emailService.sendVerificationCode(email);
        return ResponseEntity.ok(Collections.singletonMap("message", "验证码已发送"));
    }

    @PostMapping("/send-admin-verification-code")
    public ResponseEntity<Map<String, String>> sendAdminVerificationCode(@RequestBody Map<String, String> request) {
        var username = request.get("username");
        var email = adminService.getEmailByUsername(username);
        if (email == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("error", "未找到当前用户名对应的邮箱"));
        }
        emailService.sendLoginVerificationCode(email);
        return ResponseEntity.ok(Collections.singletonMap("message", "验证码已发送"));
    }
}
