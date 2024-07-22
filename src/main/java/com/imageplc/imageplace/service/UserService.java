package com.imageplc.imageplace.service;

import com.imageplc.imageplace.dto.UserInfoDTO;
import com.imageplc.imageplace.entity.UserEntity;
import com.imageplc.imageplace.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public boolean notMatchPasswords(String username, String password) {
        var existsPassword = userRepository.findPasswordByUsername(username);
        if (existsPassword == null) {
            return true;
        }
        return !passwordEncoder.matches(password, existsPassword);
    }

    public byte[] getUserAvatar(String username) {
        return userRepository.findAvatarByUsername(username);
    }

    public Map<String, String> getUserInfoByUsername(String username) {
        var info = userRepository.findUserInfoByUsername(username);
        var res = new HashMap<String, String>();
        res.put("username", info.getUsername());
        res.put("email", info.getEmail());
        res.put("nickname", info.getNickname());
        res.put("resume", info.getResume());
        return res;
    }

    public void updatePersonalInfo(String username, String email, String nickname, String resume) {
        userRepository.updateUserInfo(username, nickname, email, resume);
    }

    public void updatePassword(String username, String newPassword) {
        userRepository.updatePassword(username, passwordEncoder.encode(newPassword));
    }

    public void updateAvatar(String username, byte[] avatar) {
        userRepository.updateAvatar(username, avatar);
    }

    public String getEmailByUsername(String username) {
        return userRepository.findEmailByUsername(username);
    }

    public void deleteUser(String username) {
        userRepository.deleteById(username);
    }
    public List<UserInfoDTO> getAllUsers() {
        return userRepository.findAllUsers();
    }
}
