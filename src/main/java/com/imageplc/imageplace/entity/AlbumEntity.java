package com.imageplc.imageplace.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Entity
@Table(name = "albums")
public class AlbumEntity {
    @Id
    @Column(name = "id", columnDefinition = "INT")
    private int id;
    @Column(name = "username", columnDefinition = "VARCHAR(255)")
    private String username;
    @Setter
    @Column(name = "name", columnDefinition = "VARCHAR(255)")
    private String name;

    public AlbumEntity(int id, String username, String name) {
        this.id = id;
        this.username = username;
        this.name = name;
    }

    public AlbumEntity() {

    }
}
