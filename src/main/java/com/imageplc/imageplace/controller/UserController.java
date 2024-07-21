package com.imageplc.imageplace.controller;

import com.imageplc.imageplace.components.JwtTokenProvider;
import com.imageplc.imageplace.service.EmailService;
import com.imageplc.imageplace.service.UserService;
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
public class UserController {
    private final JwtTokenProvider tokenProvider = new JwtTokenProvider();
    @Autowired
    private UserService userService;
    @Autowired
    private VerificationService verificationService;
    @Autowired
    private EmailService emailService;

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> registerUser(@RequestBody Map<String, String> request) {
        var username = request.get("username");
        var password = request.get("password");
        var email = request.get("email");
        var verificationCode = request.get("verification_code");
        if (verificationService.notMatchVerifyCode(email, verificationCode)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("error", "验证码无效"));
        } else if (userService.userExists(username)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("error", "用户名已存在"));
        }
        userService.registerUser(username, password, email);
        return ResponseEntity.ok(Collections.singletonMap("message", "注册成功"));
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> loginUser(@RequestBody Map<String, String> request) {
        var username = request.get("username");
        var password = request.get("password");
        if (username == null || userService.notMatchPasswords(username, password)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap("error", "用户名或密码无效"));
        }
        var token = tokenProvider.generateToken(username);
        return ResponseEntity.ok(Collections.singletonMap("token", token));
    }

    @PostMapping("/get-avatar")
    public ResponseEntity<Map<String, String>> getAvatar(@RequestBody Map<String, String> request) {
        var token = request.get("token");
        if (!tokenProvider.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap("error", "Token无效"));
        }
        var username = tokenProvider.getUsernameFromToken(token);
        return ResponseEntity.ok(Collections.singletonMap("url", "/files/avatar/" + username));
    }

    @PostMapping("/get-personal-info")
    public ResponseEntity<Map<String, String>> getPersonalInfo(@RequestBody Map<String, String> request) {
        var token = request.get("token");
        if (!tokenProvider.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap("error", "Token无效"));
        }
        var username = tokenProvider.getUsernameFromToken(token);
        var info = userService.getUserInfoByUsername(username);
        return ResponseEntity.ok(info);
    }

    @PostMapping("/update-personal-info")
    public ResponseEntity<Map<String, String>> updatePersonalInfo(@RequestBody Map<String, String> request) {
        var token = request.get("token");
        if (!tokenProvider.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap("error", "Token无效"));
        }
        var username = tokenProvider.getUsernameFromToken(token);
        var email = request.get("email");
        var nickname = request.get("nickname");
        var resume = request.get("resume");
        userService.updatePersonalInfo(username, email, nickname, resume);
        return ResponseEntity.ok(Collections.singletonMap("message", "信息更新成功"));
    }

    @PostMapping("/update-password")
    public ResponseEntity<Map<String, String>> updatePassword(@RequestBody Map<String, String> request) {
        var token = request.get("token");
        if (!tokenProvider.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap("error", "Token无效"));
        }
        var username = tokenProvider.getUsernameFromToken(token);
        var currentPassword = request.get("current_password");
        var newPassword = request.get("new_password");
        if (userService.notMatchPasswords(username, currentPassword)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap("error", "当前密码不正确"));
        }
        userService.updatePassword(username, newPassword);
        return ResponseEntity.ok(Collections.singletonMap("message", "密码更新成功，请重新登录"));
    }

    @PostMapping("/send-find-back-vr-code")
    public ResponseEntity<Map<String, String>> sendFindBackVRCode(@RequestBody Map<String, String> request) {
        var username = request.get("username");
        var email = userService.getEmailByUsername(username);
        if (email == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("error", "用户名不存在"));
        }
        emailService.sendFindBackPasswordVerificationCode(email);
        return ResponseEntity.ok(Collections.singletonMap("message", "验证码已发送到" + email + "中，请查收"));
    }

    @PostMapping("/find-back-password")
    public ResponseEntity<Map<String, String>> findBackPassword(@RequestBody Map<String, String> request) {
        var username = request.get("username");
        var password = request.get("password");
        var vrCode = request.get("vr_code");
        var email = userService.getEmailByUsername(username);
        if (email == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("error", "用户名不存在"));
        }
        if (verificationService.notMatchVerifyCode(email, vrCode)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("error", "验证码不匹配"));
        }
        userService.updatePassword(username, password);
        return ResponseEntity.ok(Collections.singletonMap("message", "密码更新成功"));
    }
}
