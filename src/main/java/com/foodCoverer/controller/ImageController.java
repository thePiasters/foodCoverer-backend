package com.foodCoverer.controller;

import com.foodCoverer.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/image")
@CrossOrigin(origins = {"${cors_origin}"})
public class ImageController {

    @Autowired
    private ImageService imageService;

    @RequestMapping(value = "/{name}", method = RequestMethod.GET,
            produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> getImage(@PathVariable String name) {

        byte[] content = imageService.getImageContent(name);
        if (content == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return ResponseEntity
                .ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(content);
    }
}
