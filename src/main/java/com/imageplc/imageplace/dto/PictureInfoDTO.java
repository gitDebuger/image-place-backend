package com.imageplc.imageplace.dto;

import lombok.Getter;

@Getter
public class PictureInfoDTO {
    private final String uuid;
    private final String username;
    private final String title;
    private final String type;
    private final String status;
    public PictureInfoDTO(String uuid, String username, String title, String type, String status) {
        this.uuid = uuid;
        this.username = username;
        this.title = title;
        this.type = type;
        this.status = status;
    }
}
