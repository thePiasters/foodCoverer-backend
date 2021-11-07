package com.foodCoverer.model;

import org.hibernate.annotations.Type;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

@Entity
public class Tag {

    @Id
    @Type(type = "uuid-char")
    private UUID id;
    private String name;
    private String tagImageUrl;
    private String description;

    public String getTagImageUrl() {
        return tagImageUrl;
    }

    public void setTagImageUrl(String picture) {
        this.tagImageUrl = picture;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        if (this.getId() == ((Tag) obj).getId())
            return true;
        return false;
    }
}
