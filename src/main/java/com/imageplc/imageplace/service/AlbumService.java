package com.imageplc.imageplace.service;

import com.imageplc.imageplace.dto.AlbumInfoDTO;
import com.imageplc.imageplace.dto.ImageInfoDTO;
import com.imageplc.imageplace.entity.AlbumEntity;
import com.imageplc.imageplace.entity.AlbumPicturesEntity;
import com.imageplc.imageplace.repository.AlbumPicturesRepository;
import com.imageplc.imageplace.repository.AlbumRepository;
import com.imageplc.imageplace.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class AlbumService {
    @Autowired
    private AlbumRepository albumRepository;
    @Autowired
    private AlbumPicturesRepository albumPicturesRepository;
    @Autowired
    private ImageRepository imageRepository;

    public void addAlbum(String username, String albumName) {
        var album = new AlbumEntity(username, albumName);
        albumRepository.save(album);
    }

    public List<AlbumInfoDTO> getUserAllAlbums(String username) {
        return albumRepository.findAlbumsByUsername(username);
    }

    public List<ImageInfoDTO> getImagesByAlbumIdAndUsername(int albumId, String username) {
        return albumPicturesRepository.findImagesByAlbumIdAndUsername(albumId, username);
    }

    public boolean addPicturesIntoAlbum(String username, int albumId, List<String> picturesUUID) {
        var albumUsername = albumRepository.findUsernameByAlbumId(albumId);
        if (!username.equals(albumUsername)) {
            return false;
        }
        var albumPictures = new ArrayList<AlbumPicturesEntity>();
        for (var uuid : picturesUUID) {
            var imageUsername = imageRepository.findUsernameByUUID(uuid);
            if (imageUsername.equals(username)) {
                albumPictures.add(new AlbumPicturesEntity(albumId, uuid));
            }
        }
        albumPicturesRepository.saveAll(albumPictures);
        return true;
    }

    public boolean deletePicturesFromAlbum(String username, int albumId, List<String> picturesUUID) {
        var albumUsername = albumRepository.findUsernameByAlbumId(albumId);
        if (!username.equals(albumUsername)) {
            return false;
        }
        for (var uuid : picturesUUID) {
            var imageUsername = imageRepository.findUsernameByUUID(uuid);
            if (imageUsername.equals(username)) {
                albumPicturesRepository.deletePicturesFromAlbum(albumId, uuid);
            }
        }
        return true;
    }

    public boolean deleteAlbum(String username, int albumId) {
        var albumUsername = albumRepository.findUsernameByAlbumId(albumId);
        if (!username.equals(albumUsername)) {
            return false;
        }
        albumRepository.deleteAlbumEntityById(albumId);
        albumPicturesRepository.deleteAlbumPicturesEntitiesByAlbumId(albumId);
        return true;
    }

    public void renameAlbum(String username, int albumId, String albumName) {
        albumRepository.updateAlbumName(username, albumId, albumName);
    }
}
