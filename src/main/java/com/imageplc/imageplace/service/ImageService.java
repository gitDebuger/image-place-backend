package com.imageplc.imageplace.service;

import com.imageplc.imageplace.dto.ImageBaseInfoDTO;
import com.imageplc.imageplace.dto.ImageInfoDTO;
import com.imageplc.imageplace.dto.PictureInfoDTO;
import com.imageplc.imageplace.entity.ImageEntity;
import com.imageplc.imageplace.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ImageService {
    @Autowired
    private ImageRepository imageRepository;

    public void UploadImage(String uuid, String username, String title, byte[] content, String type, String status) {
        imageRepository.save(new ImageEntity(uuid, username, title, content, type, status));
    }

    public void markImageAsDeleted(String uuid) {
        imageRepository.markAsDeletedByUUID(uuid);
    }

    public ImageBaseInfoDTO getImage(String uuid) {
        return imageRepository.findImageContentByUUID(uuid);
    }

    public List<ImageInfoDTO> getImages(String username) {
        return imageRepository.findImageByUsername(username);
    }

    public String getTitleByUUID(String uuid) {
        return imageRepository.findTitleByUUID(uuid);
    }

    public void updateImageInfo(String uuid, String title, String status) {
        imageRepository.updateImageInfo(uuid, title, status);
    }

    public List<PictureInfoDTO> getAllPictures() {
        return imageRepository.findAllPictures();
    }
}
