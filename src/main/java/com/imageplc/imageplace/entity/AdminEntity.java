package com.imageplc.imageplace.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

@Getter
@Entity
@Table(name = "admins")
public class AdminEntity {
    @Id
    @Column(name = "username", columnDefinition = "VARCHAR(255)")
    private String username;
    @Column(name = "email", columnDefinition = "VARCHAR(255)")
    private String email;
}
