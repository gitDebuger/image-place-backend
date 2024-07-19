package com.imageplc.imageplace.dto;

import lombok.Getter;

@Getter
public class ImageInfoDTO {
    private final String uuid;
    private final String title;
    private final String status;
    public ImageInfoDTO(String uuid, String title, String status) {
        this.uuid = uuid;
        this.title = title;
        this.status = status;
    }
}
