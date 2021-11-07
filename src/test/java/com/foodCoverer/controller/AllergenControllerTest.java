package com.foodCoverer.controller;

import com.foodCoverer.model.Allergen;
import com.foodCoverer.repository.AllergenRepository;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;


@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
class AllergenControllerTest {

    @Mock
    AllergenRepository allergensRepository;

    @InjectMocks
    AllergenController allergensController;


    @Test
    void getAllAllergensShouldReturnStatusOk() {
        List<Allergen> mockList = new ArrayList<>();
        Mockito.when(allergensRepository.findAll()).thenReturn(mockList);
        var response =  allergensController.getAllAllergens();
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assert.assertEquals(mockList,response.getBody());
    }
}