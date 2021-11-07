package com.foodCoverer.service;

import com.foodCoverer.model.Category;
import com.foodCoverer.model.Subcategory;
import com.foodCoverer.repository.CategoryRepository;
import com.foodCoverer.repository.SubcategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private SubcategoryRepository subcategoryRepository;


    public Map<String, List<Subcategory>> getAllCategoriesWithSubcategories() {
        Map<String, List<Subcategory>> map = new HashMap<>();
        for (Category category : categoryRepository.findAll()) {

            map.put(category.getName(), subcategoryRepository.findAllByCategory(category));
        }
        return map;
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }
}
