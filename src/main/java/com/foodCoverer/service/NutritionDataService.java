package com.foodCoverer.service;


import com.foodCoverer.model.NutritionData;
import com.foodCoverer.model.Product;
import com.foodCoverer.repository.NutriDataRepository;
import com.foodCoverer.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class NutritionDataService {

    @Autowired
    NutriDataRepository nutriDataRepository;

    @Autowired
    ProductRepository productRepository;

    public NutritionData createNutritionData(NutritionData nutritionData) {
        UUID uuid = UUID.randomUUID();
        nutritionData.setId(uuid);
        nutriDataRepository.saveAndFlush(nutritionData);
        return nutritionData;
    }

    public NutritionData updateProductNutritionData(Long productId, NutritionData nutritionData) {

       Product savedProduct = productRepository.findById(productId).get();


        if (savedProduct.getNutritionData() == null) {
            UUID uuid = UUID.randomUUID();
            nutritionData.setId(uuid);
        }else{
            UUID nutriDataUUID = savedProduct.getNutritionData().getId();
            nutritionData.setId(nutriDataUUID);
        }
        nutriDataRepository.saveAndFlush(nutritionData);
        return nutritionData;
    }
}
