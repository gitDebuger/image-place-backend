package com.imageplc.imageplace.repository;

import com.imageplc.imageplace.entity.AdminEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<AdminEntity, String> {
    @Query("SELECT admin.email FROM AdminEntity admin WHERE admin.username = :username")
    String findEmailByUsername(@Param("username") String username);
}
