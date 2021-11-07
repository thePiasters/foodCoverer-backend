package com.foodCoverer.repository;

import com.foodCoverer.model.NutritionData;
import com.foodCoverer.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface NutriDataRepository extends JpaRepository<NutritionData, UUID> {

    @Override
    <S extends NutritionData> S saveAndFlush(S entity);
}
