package com.imageplc.imageplace.repository;

import com.imageplc.imageplace.dto.UserInfoDTO;
import com.imageplc.imageplace.entity.UserEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {
    @Query("SELECT user.password FROM UserEntity user WHERE user.username = :username")
    String findPasswordByUsername(@Param("username") String username);

    @Query("SELECT user.headPortrait FROM UserEntity user WHERE user.username = :username")
    byte[] findAvatarByUsername(@Param("username") String username);

    @Query("SELECT new com.imageplc.imageplace.dto.UserInfoDTO(user.username, user.email, user.nickname, user.resume) FROM UserEntity user WHERE user.username = :username")
    UserInfoDTO findUserInfoByUsername(@Param("username") String username);

    @Transactional
    @Modifying
    @Query("UPDATE UserEntity user SET user.email = :email, user.nickname = :nickname, user.resume = :resume WHERE user.username = :username")
    void updateUserInfo(@Param("username") String username, @Param("nickname") String nickname, @Param("email") String email, @Param("resume") String resume);

    @Transactional
    @Modifying
    @Query("UPDATE UserEntity user SET user.password = :password WHERE user.username = :username")
    void updatePassword(@Param("username") String username, @Param("password") String password);

    @Transactional
    @Modifying
    @Query("UPDATE UserEntity user SET user.headPortrait = :avatar WHERE user.username = :username")
    void updateAvatar(@Param("username") String username, @Param("avatar") byte[] avatar);
}
