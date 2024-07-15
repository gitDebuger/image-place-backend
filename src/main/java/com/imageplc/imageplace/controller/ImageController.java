package com.imageplc.imageplace.controller;

import com.imageplc.imageplace.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/files")
public class ImageController {
    @Autowired
    private UserService userService;
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
}
