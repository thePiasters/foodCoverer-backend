package com.foodCoverer.service;

import com.foodCoverer.model.Category;
import com.foodCoverer.model.Subcategory;
import com.foodCoverer.repository.CategoryRepository;
import com.foodCoverer.repository.SubcategoryRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
class CategoryServiceTest {

    @Mock
    CategoryRepository categoryRepository;

    @Mock
    SubcategoryRepository subcategoryRepository;

    @InjectMocks
    private CategoryService categoryService;


    private List<Category> categoryList;

    private static final String SUBCATEGORY_NAME  =  "test-subcategory-name";
    private static final String CATEGORY_NAME = "category-name";



    @Test
    void shouldReturnAllCategoriesWithSubcategories() {
        categoryList = new ArrayList<>();
        Category mockCategory = new Category();
        mockCategory.setName(CATEGORY_NAME);
        categoryList.add(mockCategory);

        Subcategory subcategory = new Subcategory();
        subcategory.setName(SUBCATEGORY_NAME);
        subcategory.setCategory(mockCategory);
        var subcategoryList = List.of(subcategory);


        when(categoryRepository.findAll()).thenReturn(categoryList);
        when(subcategoryRepository.findAllByCategory(mockCategory)).thenReturn(subcategoryList);
        var subcategories = categoryService.getAllCategoriesWithSubcategories().get(CATEGORY_NAME);
        Assert.assertEquals(subcategories.get(0).getName(), SUBCATEGORY_NAME);


    }

    @Test
    void getAllCategories() {
        categoryService.getAllCategories();
        verify(categoryRepository).findAll();
    }
}