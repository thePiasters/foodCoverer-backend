package com.foodCoverer.controller.integration;

import com.foodCoverer.model.Category;
import com.foodCoverer.model.Subcategory;
import com.foodCoverer.service.CategoryService;
import com.foodCoverer.service.ProductService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class CategoryControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;

    private static final String CATEGORY_NAME = "TEST_CATEGORY";


    @Test
    public void getAllCategoriesShouldReturnOk() throws Exception {

        Category category = new Category();
        category.setName(CATEGORY_NAME);
        when(categoryService.getAllCategories()).thenReturn(List.of(category));

        MvcResult result = mockMvc.perform(get("/categories/all")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        assertTrue(result.getResponse().getContentAsString().contains(CATEGORY_NAME));
    }

    @Test
    public void getAllCategoriesWithSubcategoriesShouldReturnOk() throws Exception {

        when(categoryService.getAllCategoriesWithSubcategories()).thenReturn(Map.of(CATEGORY_NAME, List.of(new Subcategory())));

        MvcResult result = mockMvc.perform(get("/categories/all/subcategories")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        assertTrue(result.getResponse().getContentAsString().contains(CATEGORY_NAME));
    }
}
