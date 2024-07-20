package com.imageplc.imageplace.repository;

import com.imageplc.imageplace.dto.AlbumInfoDTO;
import com.imageplc.imageplace.entity.AlbumEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface AlbumRepository extends JpaRepository<AlbumEntity, Integer> {
    @Query("SELECT new com.imageplc.imageplace.dto.AlbumInfoDTO(album.id, album.name) FROM AlbumEntity album WHERE album.username = :username")
    List<AlbumInfoDTO> findAlbumsByUsername(@Param("username") String username);

    @Query("SELECT album.username FROM AlbumEntity album WHERE album.id = :albumId")
    String findUsernameByAlbumId(@Param("albumId") int albumId);

    @Transactional
    @Modifying
    @Query("DELETE FROM AlbumEntity album WHERE album.id = :albumId")
    void deleteAlbumEntityById(@Param("albumId") int albumId);

    @Transactional
    @Modifying
    @Query("UPDATE AlbumEntity album SET album.name = :albumName WHERE album.username = :username AND album.id = :albumId")
    void updateAlbumName(@Param("username") String username, @Param("albumId") int albumId, @Param("albumName") String albumName);
}
