package com.imageplc.imageplace.controller;

import com.imageplc.imageplace.components.JwtTokenProvider;
import com.imageplc.imageplace.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/files")
public class ImageController {
    @Autowired
    private UserService userService;
    private final JwtTokenProvider tokenProvider = new JwtTokenProvider();

    @GetMapping("/avatar/{username}")
    public void getAvatar(@PathVariable String username, HttpServletResponse response) {
        var avatar = userService.getUserAvatar(username);
        try {
            if (avatar == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            } else {
                response.setContentType("image/jpeg");
                response.setContentLength(avatar.length);
                response.getOutputStream().write(avatar);
                response.getOutputStream().close();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @PostMapping("/avatar/update-avatar")
    public ResponseEntity<Map<String, String>> updateAvatar(@RequestParam("file") MultipartFile file, @RequestParam("token") String token) {
        if (!tokenProvider.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap("error", "Token无效"));
        }
        if (file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("error", "上传的图片不能为空"));
        }
        try {
            var username = tokenProvider.getUsernameFromToken(token);
            var content = file.getBytes();
            userService.updateAvatar(username, content);
            return ResponseEntity.ok(Collections.singletonMap("message", "头像更新成功"));
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("error", "头像更新失败"));
        }
    }
}