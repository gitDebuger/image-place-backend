package com.imageplc.imageplace.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "album_pictures")
public class AlbumPicturesEntity {
    @Id
    @Column(name = "id", columnDefinition = "INT")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "album_id", columnDefinition = "INT")
    private int albumId;
    @Column(name = "picture_uuid", columnDefinition = "CHAR(36)")
    private String pictureUUID;

    public AlbumPicturesEntity(int albumId, String pictureUUID) {
        this.albumId = albumId;
        this.pictureUUID = pictureUUID;
    }

    public AlbumPicturesEntity() {

    }
}
