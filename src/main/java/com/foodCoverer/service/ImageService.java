package com.foodCoverer.service;

import com.foodCoverer.model.Image;
import com.foodCoverer.model.ImageSource;
import com.foodCoverer.model.Product;
import com.foodCoverer.repository.ImageRepository;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

@Service
public class ImageService {

    private static final String ROOT_FOLDER = "products";
    private static final String IMAGE_PATH_PREFIX = "/image/";
    private final Path root = Paths.get(ROOT_FOLDER);

    private String constructPath(String name) {
        return ROOT_FOLDER + "/" + name;
    }

    @Autowired
    ImageRepository imageRepository;

    public byte[] getImageContent(String name) {
        Path path = Paths.get(constructPath(name));

        byte[] content = null;
        try {
            content = Files.readAllBytes(path);
        } catch (final IOException e) {
            e.printStackTrace();
        }

        return content;
    }

    private void handlePreviousImage(Optional<Image> imageOptional) {
        if (imageOptional.isPresent()) {
            Image image = imageOptional.get();
            if (image.getImageSource() == ImageSource.SERVER) {
                try {
                    Files.delete(this.root.resolve(image.getImageUrl().replace(IMAGE_PATH_PREFIX, "")));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private UUID getImageUUID(Optional<Image> image) {
        if (image.isPresent()) {
            return image.get().getId();
        }
        return UUID.randomUUID();

    }

    public Image updateServerImage(MultipartFile file, Product product) throws IOException {
        Optional<Image> imageOptional = Optional.ofNullable(product.getImage());
        handlePreviousImage(imageOptional);

        return createServerImage(file, getImageUUID(imageOptional));

    }

    public Image createServerImage(MultipartFile file, UUID uuid) throws IOException {
        UUID imageUuid = uuid;
        String file_name = imageUuid + "." + FilenameUtils.getExtension(file.getOriginalFilename());
        Files.copy(file.getInputStream(), this.root.resolve(file_name));
        Image image = new Image();
        image.setImageUrl(IMAGE_PATH_PREFIX + file_name);

        image.setId(imageUuid);
        image.setImageSource(ImageSource.SERVER);
        imageRepository.saveAndFlush(image);
        return image;
    }


    public Image createImageLink(String link, UUID uuid) {
        UUID imageUuid = uuid;
        Image image = new Image();
        image.setId(imageUuid);
        image.setImageUrl(link);
        image.setImageSource(ImageSource.EXTERNAL_LINK);
        imageRepository.saveAndFlush(image);
        return image;
    }

    public Image updateImageLink(String link, Product product) {

        Optional<Image> imageOptional = Optional.ofNullable(product.getImage());
        handlePreviousImage(imageOptional);
        return createImageLink(link, getImageUUID(imageOptional));
    }
}
