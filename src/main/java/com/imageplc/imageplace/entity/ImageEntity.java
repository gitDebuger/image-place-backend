package com.imageplc.imageplace.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Entity
@Table(name = "pictures")
public class ImageEntity {
    @Id
    @Column(name = "uuid", columnDefinition = "CHAR(36)")
    private String uuid;
    @Column(name = "username", columnDefinition = "VARCHAR(255)")
    private String username;
    @Setter
    @Column(name = "title", columnDefinition = "VARCHAR(255)")
    private String title;
    @Column(name = "content", columnDefinition = "MEDIUMBLOB")
    private byte[] content;
    @Column(name = "type", columnDefinition = "CHAR(15)")
    private String type;
    @Setter
    @Column(name = "status", columnDefinition = "ENUM('normal', 'deleted', 'cannot-get')")
    private String status;

    public ImageEntity(String uuid, String username, String title, byte[] content, String type, String status) {
        this.uuid = uuid;
        this.username = username;
        this.title = title;
        this.content = content;
        this.type = type;
        this.status = status;
    }

    public ImageEntity() {

    }
}
