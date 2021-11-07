package com.foodCoverer.repository;

import com.foodCoverer.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TagRepository extends JpaRepository<Tag, UUID> {

    @Override
    List<Tag> findAll();


    @Override
    Optional<Tag> findById(UUID id);

    @Override
    void delete(Tag tag);

    @Override
    <S extends Tag> S saveAndFlush(S entity);
}
