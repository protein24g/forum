package com.example.forum.base.image.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ImageResponse {
    private String fileName;

    @Builder
    public ImageResponse(String fileName){
        this.fileName = fileName;
    }
}
