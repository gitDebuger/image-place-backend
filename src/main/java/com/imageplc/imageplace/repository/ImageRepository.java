package com.imageplc.imageplace.repository;

import com.imageplc.imageplace.entity.ImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ImageRepository extends JpaRepository<ImageEntity, String> {
    @Transactional
    @Modifying
    @Query("UPDATE ImageEntity image SET image.status = 'deleted' WHERE image.uuid = :uuid")
    void markAsDeletedByUUID(@Param("uuid") String uuid);
}
