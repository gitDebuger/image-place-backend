package com.imageplc.imageplace.controller;

import com.imageplc.imageplace.components.JwtTokenProvider;
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

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> registerUser(@RequestBody Map<String, String> request) {
        var username = request.get("username");
        var password = request.get("password");
        var email = request.get("email");
        var verificationCode = request.get("verification_code");
        if (!verificationService.verifyCode(email, verificationCode)) {
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
}
