package com.foodCoverer.repository;

import com.foodCoverer.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CategoryRepository extends JpaRepository<Category, UUID> {

    @Override
    List<Category> findAll();

    @Override
    Optional<Category> findById(UUID aLong);

    @Override
    <S extends Category> S saveAndFlush(S entity);

    @Override
    void delete(Category category);

}
