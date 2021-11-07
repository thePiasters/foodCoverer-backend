package com.foodCoverer.model;

import org.hibernate.annotations.Type;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

@Entity
public class Image {

    @Id
    @Type(type = "uuid-char")
    private UUID id;

    ImageSource imageSource;
    String imageUrl;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public ImageSource getImageSource() {
        return imageSource;
    }

    public void setImageSource(ImageSource imageSource) {
        this.imageSource = imageSource;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String pictureUrl) {
        this.imageUrl = pictureUrl;
    }
}
