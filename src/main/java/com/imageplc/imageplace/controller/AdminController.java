package com.imageplc.imageplace.controller;

import com.imageplc.imageplace.components.JwtTokenProvider;
import com.imageplc.imageplace.service.AdminService;
import com.imageplc.imageplace.service.VerificationService;
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
public class AdminController {
    private final JwtTokenProvider tokenProvider = new JwtTokenProvider();
    @Autowired
    private AdminService adminService;
    @Autowired
    private VerificationService verificationService;

    @PostMapping("/admin-login")
    public ResponseEntity<Map<String, String>> adminLogin(@RequestBody Map<String, String> request) {
        var username = request.get("username");
        var verification_code = request.get("verification_code");
        var email = adminService.getEmailByUsername(username);
        if (email == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("error", "用户名无效"));
        }
        if (verificationService.notMatchVerifyCode(email, verification_code)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("error", "验证码错误"));
        }
        var token = tokenProvider.generateToken(username);
        return ResponseEntity.ok(Collections.singletonMap("token", token));
    }

    @PostMapping("/get-admin-username-by-token")
    public ResponseEntity<Map<String, String>> getAdminUsernameByToken(@RequestBody Map<String, String> request) {
        var token = request.get("token");
        if (!tokenProvider.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap("error", "Token无效"));
        }
        var username = tokenProvider.getUsernameFromToken(token);
        return ResponseEntity.ok(Collections.singletonMap("username", username));
    }
}
