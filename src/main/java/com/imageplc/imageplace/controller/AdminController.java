package com.imageplc.imageplace.controller;

import com.imageplc.imageplace.components.JwtTokenProvider;
import com.imageplc.imageplace.dto.PictureInfoDTO;
import com.imageplc.imageplace.dto.UserInfoDTO;
import com.imageplc.imageplace.service.AdminService;
import com.imageplc.imageplace.service.ImageService;
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
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class AdminController {
    private final JwtTokenProvider tokenProvider = new JwtTokenProvider();
    @Autowired
    private AdminService adminService;
    @Autowired
    private VerificationService verificationService;
    @Autowired
    private UserService userService;
    @Autowired
    private ImageService imageService;

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

    @PostMapping("/admin-change-user-info")
    public ResponseEntity<Map<String, String>> adminChangeUserInfo(@RequestBody Map<String, String> request) {
        var token = request.get("token");
        if (!tokenProvider.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap("error", "Token无效"));
        }
        var admin = tokenProvider.getUsernameFromToken(token);
        if (!adminService.validateAdmin(admin)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap("error", "无权限操作"));
        }
        var username = request.get("username");
        var nickname = request.get("nickname");
        var email = request.get("email");
        var resume = request.get("resume");
        userService.updatePersonalInfo(username, email, nickname, resume);
        return ResponseEntity.ok(Collections.singletonMap("message", "修改成功"));
    }

    @PostMapping("/admin-change-user-password")
    public ResponseEntity<Map<String, String>> adminChangeUserPassword(@RequestBody Map<String, String> request) {
        var token = request.get("token");
        if (!tokenProvider.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap("error", "Token无效"));
        }
        var admin = tokenProvider.getUsernameFromToken(token);
        if (!adminService.validateAdmin(admin)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap("error", "无权限操作"));
        }
        var username = request.get("username");
        var password = request.get("password");
        userService.updatePassword(username, password);
        return ResponseEntity.ok(Collections.singletonMap("message", "密码更新成功"));
    }

    @PostMapping("/admin-delete-user")
    public ResponseEntity<Map<String, String>> adminDeleteUser(@RequestBody Map<String, String> request) {
        var token = request.get("token");
        if (!tokenProvider.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap("error", "Token无效"));
        }
        var admin = tokenProvider.getUsernameFromToken(token);
        if (!adminService.validateAdmin(admin)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap("error", "无权限操作"));
        }
        var username = request.get("username");
        userService.deleteUser(username);
        return ResponseEntity.ok(Collections.singletonMap("message", "删除用户成功"));
    }

    @PostMapping("/admin-fetch-all-users")
    public ResponseEntity<List<UserInfoDTO>> adminFetchAllUsers(@RequestBody Map<String, String> request) {
        var token = request.get("token");
        if (!tokenProvider.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonList(null));
        }
        var admin = tokenProvider.getUsernameFromToken(token);
        if (!adminService.validateAdmin(admin)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonList(null));
        }
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PostMapping("/admin-fetch-all-pictures")
    public ResponseEntity<List<PictureInfoDTO>> adminFetchAllPictures(@RequestBody Map<String, String> request) {
        var token = request.get("token");
        if (!tokenProvider.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonList(null));
        }
        var admin = tokenProvider.getUsernameFromToken(token);
        if (!adminService.validateAdmin(admin)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonList(null));
        }
        return ResponseEntity.ok(imageService.getAllPictures());
    }

    @PostMapping("/admin-update-picture-info")
    public ResponseEntity<Map<String, String>> adminUpdatePictureInfo(@RequestBody Map<String, String> request) {
        var token = request.get("token");
        if (!tokenProvider.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap("error", "Token无效"));
        }
        var admin = tokenProvider.getUsernameFromToken(token);
        if (!adminService.validateAdmin(admin)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap("error", "无权限操作"));
        }
        var uuid = request.get("uuid");
        var status = request.get("status");
        var title = request.get("title");
        imageService.updateImageInfo(uuid, title, status);
        System.out.println(uuid);
        return ResponseEntity.ok(Collections.singletonMap("message", "信息更新成功"));
    }
}
