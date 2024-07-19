package com.imageplc.imageplace.service;

import com.imageplc.imageplace.entity.ImageEntity;
import com.imageplc.imageplace.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
