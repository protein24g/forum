package com.example.forum.base.image.controller;

import com.example.forum.base.image.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class ImageController {
    private final ImageService imageService;

    @GetMapping("/image/{member_id}")
    public ResponseEntity<byte[]> returnImage(@PathVariable("member_id") Long id) {
        return null;
    }
}
