package com.foodCoverer.controller;

import com.foodCoverer.model.Category;
import com.foodCoverer.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/categories")
@CrossOrigin("http://localhost:3000")
public class CategoryController {


    @Autowired
    private CategoryService categoryService;

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public ResponseEntity<List<Category>> getAllCategories() {
        return ResponseEntity.ok().body(categoryService.getAllCategories());
    }


    @RequestMapping(value = "/all/subcategories",  method = RequestMethod.GET)
    public ResponseEntity<Object> getAllCategoriesWithSubcategories() {

        var map = categoryService.getAllCategoriesWithSubcategories();
        return ResponseEntity.ok().body(map);
    }
}
