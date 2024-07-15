package com.imageplc.imageplace.service;

import com.imageplc.imageplace.entity.UserEntity;
import com.imageplc.imageplace.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public void registerUser(String username, String password, String email) {
        var user = new UserEntity(username, passwordEncoder.encode(password), email);
        try {
            var defaultAvatar = Files.readAllBytes(Path.of("src/main/resources/static/default-avatar.jpg"));
            user.setHeadPortrait(defaultAvatar);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        userRepository.save(user);
    }

    public boolean userExists(String username) {
        return userRepository.existsById(username);
    }

    public boolean matchPasswords(String username, String password) {
        return passwordEncoder.matches(
                password,
                userRepository.findUserEntityByUsername(username).getPassword()
        );
    }

    public byte[] getUserAvatar(String username) {
        return userRepository.findUserEntityByUsername(username).getHeadPortrait();
    }
}
