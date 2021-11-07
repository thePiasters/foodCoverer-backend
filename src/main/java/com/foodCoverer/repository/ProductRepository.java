package com.foodCoverer.repository;


import com.foodCoverer.model.*;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Override
    List<Product> findAll();

    @Override
    <S extends Product> S saveAndFlush(S entity);

    @Override
    Optional<Product> findById(Long barcode);

    List<Product> findBySubcategory(Subcategory subcategory);

    List<Product> findProductsByGradeIsLessThanAndSubcategory(char nutriscoreGrade, Subcategory subcategory);

    Set<Product> findProductsByAllergensNotContaining(Allergen allergen);

    List<Product> findProductsByTagsContaining(Tag tag);

    List<Product> findProductsByIsVerified(Boolean isVerified);

    List<Product> findProductsByBrand(Brand brand);

    List<Product> findProductsByUser(User user);

    @Override
    void delete(Product entity);

}
