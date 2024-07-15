package com.imageplc.imageplace.service;

import com.imageplc.imageplace.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService {
    @Autowired
    private AdminRepository adminRepository;
    public String getEmailByUsername(String username) {
        var admin = adminRepository.findAdminEntityByUsername(username);
        if (admin == null) {
            return null;
        }
        return admin.getEmail();
    }
}
