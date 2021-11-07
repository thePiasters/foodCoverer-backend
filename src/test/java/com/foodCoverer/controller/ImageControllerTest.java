package com.foodCoverer.controller;

import com.foodCoverer.service.ImageService;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
class ImageControllerTest {

    private static final String IMAGE_PATH = "test/test-image.jpg";
    private static final String FULL_IMAGE_PATH = "products/test/test-image.jpg";
    private static final String NON_EXISTING_IMAGE = "dummy_name";


    @Mock
    ImageService imageService;

    @InjectMocks
    ImageController imageController;

    private byte[] getFileContent(){
        Path path = Paths.get(FULL_IMAGE_PATH);
        byte[] content = null;
        try {
            content = Files.readAllBytes(path);
        } catch (final IOException e) {
            e.printStackTrace();
        }
        System.out.println(content);
        return content;
    }

    @Test
    void getImageShouldReturnNoContent() {
        Mockito.when(imageService.getImageContent(NON_EXISTING_IMAGE)).thenReturn(null);
        assertEquals(HttpStatus.NO_CONTENT ,imageController.getImage(NON_EXISTING_IMAGE).getStatusCode());
    }

    @Test
    void getImageShouldReturnStatusOk() {
        Mockito.when(imageService.getImageContent(IMAGE_PATH)).thenReturn(getFileContent());
        ResponseEntity<byte[]> response = imageController.getImage(IMAGE_PATH);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        boolean result = Arrays.equals(getFileContent(),response.getBody());
        assertTrue(result);
    }


}