package com.imageplc.imageplace.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Entity
@Table(name = "users")
public class UserEntity {
    @Id
    @Column(name = "username", columnDefinition = "VARCHAR(255)")
    private String username;
    @Setter
    @Column(name = "password", columnDefinition = "VARCHAR(255)")
    private String password;
    @Setter
    @Column(name = "nickname", columnDefinition = "VARCHAR(255)")
    private String nickname;
    @Setter
    @Column(name = "email", columnDefinition = "VARCHAR(255)")
    private String email;
    @Setter
    @Column(name = "head_portrait", columnDefinition = "MEDIUMBLOB")
    private byte[] headPortrait;
    @Setter
    @Column(name = "resume", columnDefinition = "VARCHAR(1023)")
    private String resume;

    public UserEntity() {
    }

    public UserEntity(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.nickname = null;
        this.email = email;
        this.headPortrait = null;
        this.resume = null;
    }
}
