package com.imageplc.imageplace.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Entity
@Table(name = "albums")
public class AlbumEntity {
    @Id
    @Column(name = "id", columnDefinition = "INT")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "username", columnDefinition = "VARCHAR(255)")
    private String username;
    @Setter
    @Column(name = "name", columnDefinition = "VARCHAR(255)")
    private String name;

    public AlbumEntity(String username, String name) {
        this.username = username;
        this.name = name;
    }

    public AlbumEntity() {

    }
}
