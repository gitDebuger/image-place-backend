package com.imageplc.imageplace.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

@Getter
@Entity
@Table(name = "album_pictures")
public class AlbumPicturesEntity {
    @Id
    @Column(name = "id", columnDefinition = "INT")
    private int id;
    @Column(name = "album_id", columnDefinition = "INT")
    private int albumId;
    @Column(name = "picture_uuid", columnDefinition = "CHAR(36)")
    private String pictureUUID;

    public AlbumPicturesEntity(int id, int albumId, String pictureUUID) {
        this.id = id;
        this.albumId = albumId;
        this.pictureUUID = pictureUUID;
    }

    public AlbumPicturesEntity() {

    }
}
