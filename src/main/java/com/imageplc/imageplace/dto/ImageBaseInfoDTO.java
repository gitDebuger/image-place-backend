package com.imageplc.imageplace.dto;

import lombok.Getter;

@Getter
public class ImageBaseInfoDTO {
    private final byte[] content;
    private final String type;
    public ImageBaseInfoDTO(byte[] content, String type) {
        this.content = content;
        this.type = type;
    }
}
