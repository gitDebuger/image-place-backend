package com.imageplc.imageplace.controller;

import com.imageplc.imageplace.components.JwtTokenProvider;
import com.imageplc.imageplace.service.ImageService;
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
import java.util.UUID;

@RestController
@RequestMapping("/files")
public class ImageController {
    private final JwtTokenProvider tokenProvider = new JwtTokenProvider();
    @Autowired
    private UserService userService;
    @Autowired
    private ImageService imageService;

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

    @PostMapping("/image/upload")
    public ResponseEntity<Map<String, String>> uploadImage(@RequestParam("file") MultipartFile file, @RequestParam("token") String token) {
        if (!tokenProvider.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap("error", "Token无效"));
        }
        if (file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("error", "上传的图片不能为空"));
        }
        try {
            var uuid = UUID.randomUUID().toString();
            var username = tokenProvider.getUsernameFromToken(token);
            var title = file.getOriginalFilename();
            var content = file.getBytes();
            var type = file.getContentType();
            var status = "normal";
            imageService.UploadImage(uuid, username, title, content, type, status);
            return ResponseEntity.ok(Collections.singletonMap("uuid", uuid));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("error", "上传失败"));
    }

    @DeleteMapping("/image/revert/{uuid}")
    public ResponseEntity<Map<String, String>> revertFile(@PathVariable String uuid, @RequestBody Map<String, String> request) {
        var token = request.get("token");
        if (!tokenProvider.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap("error", "Token无效"));
        }
        imageService.markImageAsDeleted(uuid);
        return ResponseEntity.ok(Collections.singletonMap("message", "删除成功"));
    }
}