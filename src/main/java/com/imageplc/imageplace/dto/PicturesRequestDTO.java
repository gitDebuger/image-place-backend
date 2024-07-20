package com.imageplc.imageplace.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PicturesRequestDTO {
    private String token;
    private String album;
    private List<String> pictures;
}
