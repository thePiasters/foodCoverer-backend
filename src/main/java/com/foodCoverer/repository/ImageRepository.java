package com.foodCoverer.repository;

import com.foodCoverer.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.UUID;

@Repository
public interface ImageRepository extends JpaRepository<Image, UUID> {

    @Override
    <S extends Image> S saveAndFlush(S entity);

    @Override
    void delete(Image entity);
}
