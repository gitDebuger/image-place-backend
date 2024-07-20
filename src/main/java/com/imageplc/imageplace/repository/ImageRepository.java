package com.imageplc.imageplace.repository;

import com.imageplc.imageplace.dto.ImageBaseInfoDTO;
import com.imageplc.imageplace.dto.ImageInfoDTO;
import com.imageplc.imageplace.entity.ImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<ImageEntity, String> {
    @Transactional
    @Modifying
    @Query("UPDATE ImageEntity image SET image.status = 'deleted' WHERE image.uuid = :uuid")
    void markAsDeletedByUUID(@Param("uuid") String uuid);

    @Query("SELECT new com.imageplc.imageplace.dto.ImageBaseInfoDTO(image.content, image.type) FROM ImageEntity image WHERE image.uuid = :uuid AND image.status = 'normal'")
    ImageBaseInfoDTO findImageContentByUUID(@Param("uuid") String uuid);

    @Query("SELECT new com.imageplc.imageplace.dto.ImageInfoDTO(image.uuid, image.title, image.status) FROM ImageEntity image WHERE image.username = :username AND image.status != 'deleted'")
    List<ImageInfoDTO> findImageByUsername(@Param("username") String username);

    @Query("SELECT image.title FROM ImageEntity image WHERE image.uuid = :uuid AND image.status = 'normal'")
    String findTitleByUUID(@Param("uuid") String uuid);

    @Transactional
    @Modifying
    @Query("UPDATE ImageEntity image SET image.title = :title, image.status = :status WHERE image.uuid = :uuid")
    void updateImageInfo(@Param("uuid") String uuid, @Param("title") String title, @Param("status") String status);

    @Query("SELECT image.username FROM ImageEntity image WHERE image.uuid = :imageUUID")
    String findUsernameByUUID(@Param("imageUUID") String imageUUID);
}
