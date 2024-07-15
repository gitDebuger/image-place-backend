package com.imageplc.imageplace.repository;

import com.imageplc.imageplace.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, String> {
    UserEntity findUserEntityByUsername(String username);
}
