package com.imageplc.imageplace.repository;

import com.imageplc.imageplace.entity.AdminEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<AdminEntity, String> {
    AdminEntity findAdminEntityByUsername(String username);
}
