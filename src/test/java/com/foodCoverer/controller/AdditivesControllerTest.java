package com.foodCoverer.controller;

import com.foodCoverer.model.Additive;
import com.foodCoverer.repository.AdditivesRepository;
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
class AdditivesControllerTest {

    @Mock
    AdditivesRepository additivesRepository;

    @InjectMocks
    AdditivesController additivesController;

    @Test
    void getAllAdditivesShouldReturnStatusOk() {
        List<Additive> mockList = new ArrayList<>();
        Mockito.when(additivesRepository.findAll()).thenReturn(mockList);
        var response =  additivesController.getAllAdditives();
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assert.assertEquals(mockList,response.getBody());
    }
}