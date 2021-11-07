package com.foodCoverer.service;

import com.foodCoverer.model.NutritionData;
import com.foodCoverer.model.Product;
import com.foodCoverer.repository.NutriDataRepository;
import com.foodCoverer.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
class NutritionDataServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private NutriDataRepository nutriDataRepository;

    @InjectMocks
    private NutritionDataService nutritionDataService;

    private static final double CARBS_VALUE = 13.4;
    private static final long PRODUCT_ID = Long.valueOf("343434");

    @Test
    void createNutritionDataShouldSaveAndFlushEntity() {

        NutritionData nutritionData = new NutritionData();
        nutritionData.setCarbohydrates(CARBS_VALUE);
        var result = nutritionDataService.createNutritionData(nutritionData);
        assertEquals(nutritionData, result );
        assertEquals(CARBS_VALUE, nutritionData.getCarbohydrates());
        assertNotNull(result.getId());
        verify(nutriDataRepository).saveAndFlush(nutritionData);
    }
    @Test
    void updateProductNutritionDataShouldModifyProductNutritionData(){

        Product mockProduct = new Product();
        mockProduct.setProductId(PRODUCT_ID);
        NutritionData mockNutritionData = new NutritionData();
        UUID randomUUID = UUID.randomUUID();
        mockNutritionData.setId(randomUUID);
        mockNutritionData.setCarbohydrates(CARBS_VALUE);
        mockProduct.setNutritionData(mockNutritionData);

        Mockito.when(productRepository.findById(PRODUCT_ID)).thenReturn(Optional.of(mockProduct));

        var result = nutritionDataService.updateProductNutritionData(mockProduct.getProductId(), new NutritionData());
        assertEquals(randomUUID, result.getId());
        assertNotEquals(CARBS_VALUE, result.getCarbohydrates());
    }
}