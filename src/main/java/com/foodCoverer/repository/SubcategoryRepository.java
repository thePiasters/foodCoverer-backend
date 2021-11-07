package com.foodCoverer.repository;


import com.foodCoverer.model.Category;
import com.foodCoverer.model.Subcategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SubcategoryRepository extends JpaRepository<Subcategory, UUID> {
    @Override
    List<Subcategory> findAll();

    @Override
    Optional<Subcategory> findById(UUID id);

    @Override
    <S extends Subcategory> S saveAndFlush(S subcategory);

    @Override
    void delete(Subcategory subcategory);

    List<Subcategory> findAllByCategory(Category category);

}
