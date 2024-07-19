package com.imageplc.imageplace.repository;

import com.imageplc.imageplace.entity.AlbumPicturesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlbumPicturesRepository extends JpaRepository<AlbumPicturesEntity, Integer> {
}
