package com.foodCoverer.service;

import com.foodCoverer.model.Image;
import com.foodCoverer.model.ImageSource;
import com.foodCoverer.model.Product;
import com.foodCoverer.repository.ImageRepository;
import org.apache.commons.io.FilenameUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.junit.Assert;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.lang.Nullable;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
class ImageServiceTest {


    private static final String IMAGE_PATH = "test/test-image.jpg";
    private static final String FULL_IMAGE_PATH = "products/test/test-image.jpg";
    private static final String NON_EXISTING_IMAGE = "dummy_name";

    private static final String IMAGE_PATH_PREFFIX = "/image/";
    private static final String EXAMPLE_LINK =  "https://france-export-fv-online.com/4827-large_default/chips-bolognaise-lay-s.jpg";

    @Mock
    private ImageRepository imageRepository;

    @InjectMocks
    private ImageService imageService;


    @Test
    public void shouldReturnImageContent() throws IOException {
        Path path = Paths.get(FULL_IMAGE_PATH);
        byte[] expectedContent = Files.readAllBytes(path);
        byte[] actualContent = imageService.getImageContent(IMAGE_PATH);
        boolean res = Arrays.equals(expectedContent, actualContent);
        assertTrue(res);
    }

    @Test
    public void shouldReturnNull() {
      Assert.assertNull(imageService.getImageContent(NON_EXISTING_IMAGE));
    }

    @Test
    public void shouldCreatePictureWithLink(){

        UUID randomUUID = UUID.randomUUID();

        Image image = imageService.createImageLink(EXAMPLE_LINK, randomUUID);
        verify(imageRepository).saveAndFlush(any());
        assertEquals(image.getId(), randomUUID);
        assertEquals(image.getImageUrl(), EXAMPLE_LINK);
        assertEquals(image.getImageSource(), ImageSource.EXTERNAL_LINK);
    }

    @Test
    public void shouldCreatePictureWithMultipartFile() throws IOException {
        UUID randomUUID = UUID.randomUUID();

        MultipartFile multipartFile = new MockMultipartFile("name","original-name.jpg","", new FileInputStream(FULL_IMAGE_PATH));


        Image image = imageService.createServerImage(multipartFile, randomUUID);
        verify(imageRepository).saveAndFlush(any());

        String imageUrl = IMAGE_PATH_PREFFIX + randomUUID + ".jpg";
        String fileUrl = "products/" + randomUUID + ".jpg";

        assertEquals(randomUUID, image.getId());
        assertEquals(imageUrl, image.getImageUrl());
        File file = new File(fileUrl);
        assertTrue(file.isFile());

        FileInputStream createdFileStream = new FileInputStream(fileUrl);
        byte[] createdPicture = createdFileStream.readAllBytes();
        byte[] intialPicture = new FileInputStream(FULL_IMAGE_PATH).readAllBytes();
        assertTrue(Arrays.equals(createdPicture, intialPicture));
        createdFileStream.close();
        Files.delete(Paths.get(fileUrl));
    }

    @Test
    public void shouldUpdateProductsPictureLink() throws IOException {
        UUID randomUUID = UUID.randomUUID();


        String path = "products/" +randomUUID+ ".jpg";
        File imageFile = new File(path);
        imageFile.createNewFile();


        String imageUrl = IMAGE_PATH_PREFFIX + randomUUID + ".jpg";
        Image image = new Image();
        image.setImageSource(ImageSource.SERVER);
        image.setId(randomUUID);
        image.setImageUrl(imageUrl);
        Product product = new Product();
        product.setImage(image);

        Image resultImage = imageService.updateImageLink(EXAMPLE_LINK, product);
        verify(imageRepository).saveAndFlush(any());
        assertEquals(randomUUID, resultImage.getId());
        assertEquals(EXAMPLE_LINK, resultImage.getImageUrl());
        assertEquals(ImageSource.EXTERNAL_LINK, resultImage.getImageSource());
        assertFalse(new File(path).isFile());
    }
    @Test
    public void shouldUpdateProductsPicturePath() throws IOException {
        MultipartFile multipartFile = new MockMultipartFile("name","original-name.jpg","", new FileInputStream(FULL_IMAGE_PATH));
        UUID randomUUID = UUID.randomUUID();
        Image mockImage = new Image();
        mockImage.setId(randomUUID);

        Product mockProduct = new Product();
        mockProduct.setImage(mockImage);


        Image resultImage = imageService.updateServerImage(multipartFile,mockProduct);
        assertEquals(ImageSource.SERVER, resultImage.getImageSource());
        assertEquals(randomUUID, resultImage.getId());

        String filePath = "products/" +randomUUID+ ".jpg";
        assertTrue(new File(filePath).isFile());
        Files.delete(Paths.get(filePath));

    }







}