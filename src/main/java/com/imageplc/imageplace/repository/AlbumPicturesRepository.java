package com.imageplc.imageplace.repository;

import com.imageplc.imageplace.dto.ImageInfoDTO;
import com.imageplc.imageplace.entity.AlbumPicturesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface AlbumPicturesRepository extends JpaRepository<AlbumPicturesEntity, Integer> {
    @Query("SELECT new com.imageplc.imageplace.dto.ImageInfoDTO(image.uuid, image.title, image.status) " +
            "FROM AlbumPicturesEntity albumPictures " +
            "JOIN ImageEntity image ON image.uuid = albumPictures.pictureUUID " +
            "JOIN AlbumEntity album ON album.id = albumPictures.albumId " +
            "WHERE albumPictures.albumId = :albumId AND album.username = :username")
    List<ImageInfoDTO> findImagesByAlbumIdAndUsername(@Param("albumId") int albumId, @Param("username") String username);

    @Transactional
    @Modifying
    @Query("DELETE FROM AlbumPicturesEntity albumPictures WHERE albumPictures.albumId = :albumId AND albumPictures.pictureUUID = :pictureUUID")
    void deletePicturesFromAlbum(@Param("albumId") int albumId, @Param("pictureUUID") String pictureUUID);

    @Transactional
    @Modifying
    @Query("DELETE FROM AlbumPicturesEntity albumPictures WHERE albumPictures.albumId = :albumId")
    void deleteAlbumPicturesEntitiesByAlbumId(@Param("albumId") int albumId);
}
