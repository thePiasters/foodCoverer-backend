package com.foodCoverer.controller;

import com.foodCoverer.model.Tag;
import com.foodCoverer.repository.TagRepository;
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
class TagsControllerTest {

    @Mock
    TagRepository tagRepository;

    @InjectMocks
    TagsController tagController;

    @Test
    void getAllTagsShouldReturnNoContent() {
        List<Tag> mockList = new ArrayList<>();
        Mockito.when(tagRepository.findAll()).thenReturn(mockList);
        var response =  tagController.getAllTags();
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assert.assertEquals(mockList,response.getBody());
    }
}