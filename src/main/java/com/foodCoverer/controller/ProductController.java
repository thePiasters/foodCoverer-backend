package com.foodCoverer.controller;

import com.foodCoverer.model.*;
import com.foodCoverer.repository.ProductRepository;
import com.foodCoverer.service.ProductService;
import com.foodCoverer.session.SessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.util.*;


@RestController
@RequestMapping("/products")
@CrossOrigin(origins = {"${cors_origin}"}, allowCredentials = "true")
public class ProductController {


    @Autowired
    private ProductService productService;

    @Autowired
    private SessionManager sessionManager;

    @Autowired
    private ProductRepository productRepository;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Product> getProductById(@PathVariable("id") Long id) {

        return ResponseEntity.ok().body(productRepository.findById(id).get());
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public ResponseEntity<List<Product>> getAllProducts() {

        return ResponseEntity.ok().body(productService.getAllProducts());
    }


    @RequestMapping(value = "/create/withFile", method = RequestMethod.POST)
    public ResponseEntity createProductWithFile(@RequestPart("product") Product product,
                                                @Nullable @RequestPart(value = "file", required = false) Optional<MultipartFile> fileOptional,
                                                @Nullable @RequestPart(value = "nutriData") Optional<NutritionData> nutritionDataOptional) throws IOException {


        if (!sessionManager.isLoggedIn()) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        if (productService.checkIfExists(product)) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }


        return ResponseEntity.ok().body(productService.createProductWithFile(product, fileOptional, nutritionDataOptional));
    }

    @RequestMapping(value = "/update/withLink", method = RequestMethod.POST)
    public ResponseEntity updateProductWithLink(@RequestPart("product") Product product,
                                                @Nullable @RequestPart(value = "link") Optional<String> linkOptional,
                                                @Nullable @RequestPart(value = "nutriData") Optional<NutritionData> nutritionDataOptional) {
        if (!sessionManager.isAuthorized(product.getUser())) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        if (!productService.checkIfExists(product)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return ResponseEntity.ok().body(productService.updatePictureWithPictureLink(product, nutritionDataOptional, linkOptional));
    }

    @RequestMapping(value = "/create/withLink", method = RequestMethod.POST)
    public ResponseEntity createProductWithLink(@RequestPart("product") Product product,
                                                @Nullable @RequestPart(value = "link") Optional<String> linkOptional,
                                                @Nullable @RequestPart(value = "nutriData") Optional<NutritionData> nutritionDataOptional) {


        if (!sessionManager.isLoggedIn()) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        if (productService.checkIfExists(product)) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }


        return ResponseEntity.ok().body(productService.createProductWithLink(product, linkOptional, nutritionDataOptional));
    }

    @RequestMapping(value = "/update/withFile", method = RequestMethod.POST)
    public ResponseEntity updateProductWithFile(@RequestPart("product") Product product,
                                                @Nullable @RequestPart(value = "file", required = false) Optional<MultipartFile> fileOptional,
                                                @Nullable @RequestPart(value = "nutriData") Optional<NutritionData> nutritionDataOptional) throws IOException {

        if (!sessionManager.isAuthorized(product.getUser())) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        if (!productService.checkIfExists(product)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }


        return ResponseEntity.ok().body(productService.updateProductWithPictureFile(product, nutritionDataOptional, fileOptional));
    }


    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public ResponseEntity<Object> deleteProduct(@RequestBody Product product) {

        if (!sessionManager.isAuthorized(product.getUser())) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        if (!productService.checkIfExists(product)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        productService.deleteProduct(product);
        return new ResponseEntity<>(HttpStatus.OK);

    }


    @RequestMapping(value = "/subcategory", method = RequestMethod.POST)
    public ResponseEntity<Object> getProductBySubcategory(@RequestBody Subcategory subcategory) {
        return ResponseEntity.ok().body(productService.findProductsBySubcategory(subcategory));
    }

    @RequestMapping(value = "/category", method = RequestMethod.POST)
    public ResponseEntity<Object> getProductsByCategory(@RequestBody Category category) {

        return ResponseEntity.ok().body(productService.getProductsByCategory(category));

    }

    @RequestMapping(value = "/substitutes/grade={grade}", method = RequestMethod.POST)
    public ResponseEntity<Object> getSubstitutes(@RequestBody Subcategory subcategory, @PathVariable("grade") char grade) {

        List<Product> substitutes = productService.getProductsSubstitutes(grade, subcategory);
        return ResponseEntity.ok().body(substitutes);
    }


    @RequestMapping(value = "/notVerified", method = RequestMethod.GET)
    public ResponseEntity<Object> getNotVerifiedProducts() {

        if (sessionManager.isAdmin()) {
            return ResponseEntity.ok().body(productService.findNotVerifiedProducts());
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);

    }

    @RequestMapping(value = "/verify", method = RequestMethod.PATCH)
    public ResponseEntity<Object> verifyProduct(@RequestBody Product product) {

        if (sessionManager.isAdmin()) {
            if (productService.toggleVerifyProduct(product)) {
                return ResponseEntity.ok().body(product);
            } else {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);

    }

    @RequestMapping(value = "/byUser", method = RequestMethod.GET)
    public ResponseEntity<Object> getProductsByUser() {
        if (!sessionManager.isLoggedIn()) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        List<Product> products = productService.getProductByUser(sessionManager.getLoggedInUser());
        return ResponseEntity.ok().body(products);
    }


}
