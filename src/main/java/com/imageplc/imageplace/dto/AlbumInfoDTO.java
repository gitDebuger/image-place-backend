package com.imageplc.imageplace.dto;

import lombok.Getter;

@Getter
public class AlbumInfoDTO {
    private final int id;
    private final String name;
    public AlbumInfoDTO(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
