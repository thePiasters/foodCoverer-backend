package com.foodCoverer.service;

import com.foodCoverer.model.*;
import com.foodCoverer.repository.*;
import com.foodCoverer.session.SessionManager;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Validator;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductService {


    @Autowired
    ProductRepository productRepository;

    @Autowired
    SubcategoryRepository subcategoryRepository;


    @Autowired
    ImageRepository imageRepository;

    @Autowired
    NutritionDataService nutritionDataService;

    @Autowired
    ImageService imageService;

    @Autowired
    SessionManager sessionManager;

    @Autowired
    CategoryRepository categoryRepository;


    public List<Product> findNotVerifiedProducts() {
        return productRepository.findProductsByIsVerified(false);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public boolean checkIfExists(Product product) {
        return productRepository.existsById(product.getProductId());
    }

    public void deleteProduct(Product product) {

        if (productRepository.existsById(product.getProductId())) {
            productRepository.delete(product);
        }
    }

    public boolean toggleVerifyProduct(Product product) {
        if (productRepository.existsById(product.getProductId())) {
            product.setVerified(!product.isVerified());
            productRepository.saveAndFlush(product);
            return true;
        }
        return false;
    }

    public List<Product> getProductByUser(User user) {
        return productRepository.findProductsByUser(user);
    }

    public Product createProductWithLink(Product product,
                                         Optional<String> linkOptional,
                                         Optional<NutritionData> nutritionDataOptional) {

        if (nutritionDataOptional.isPresent()) {
            NutritionData nutritionData = nutritionDataService.createNutritionData(nutritionDataOptional.get());
            product.setNutritionData(nutritionData);
        }

        if (linkOptional.isPresent()) {
            Image image = imageService.createImageLink(linkOptional.get(), UUID.randomUUID());
            product.setImage(image);
        }

        setCurrentUserAsCreator(product);
        return productRepository.saveAndFlush(product);

    }

    public Product createProductWithFile(Product product,
                                         Optional<MultipartFile> fileOptional,
                                         Optional<NutritionData> nutritionDataOptional) throws IOException {
        if (nutritionDataOptional.isPresent()) {
            NutritionData nutritionData = nutritionDataService.createNutritionData(nutritionDataOptional.get());
            product.setNutritionData(nutritionData);
        }


        if (fileOptional.isPresent()) {
            MultipartFile file = fileOptional.get();
            Image image = imageService.createServerImage(file, UUID.randomUUID());
            product.setImage(image);
        }

        setCurrentUserAsCreator(product);

        return productRepository.saveAndFlush(product);
    }

    public Product updateProductWithPictureFile(Product product, Optional<NutritionData> nutritionDataOptional, Optional<MultipartFile> fileOptional) throws IOException {
        if (nutritionDataOptional.isPresent()) {

            NutritionData nutritionData = nutritionDataService.updateProductNutritionData(product.getProductId(), nutritionDataOptional.get());
            product.setNutritionData(nutritionData);
        }

        if (fileOptional.isPresent()) {
            MultipartFile file = fileOptional.get();
            Image image = imageService.updateServerImage(file, product);
            product.setImage(image);
        }

        setCurrentUserAsCreator(product);
        return productRepository.saveAndFlush(product);
    }

    public Product updatePictureWithPictureLink(Product product,
                                                Optional<NutritionData> nutritionDataOptional,
                                                Optional<String> linkOptional) {


        if (nutritionDataOptional.isPresent()) {
            NutritionData nutritionData = nutritionDataService.updateProductNutritionData(product.getProductId(), nutritionDataOptional.get());
            product.setNutritionData(nutritionData);
        }

        if (linkOptional.isPresent()) {
            Image image = imageService.updateImageLink(linkOptional.get(), product);
            product.setImage(image);
        }

        setCurrentUserAsCreator(product);
        return productRepository.saveAndFlush(product);
    }


    private void setCurrentUserAsCreator(Product product) {
        if (product.getUser() == null) {
            User user = sessionManager.getLoggedInUser();
            product.setUser(user);
        }
    }

    public List<Product> findProductsBySubcategory(Subcategory subcategory) {

        return productRepository.findBySubcategory(subcategory);
    }

    public List<Product> getProductsByCategory(Category category) {
        List<Product> allProducts = new ArrayList<>();
        List<Subcategory> subcategories = subcategoryRepository.findAllByCategory(category);
        for (Subcategory subcategory : subcategories) {
            List<Product> products = productRepository.findBySubcategory(subcategory);
            allProducts.addAll(products);
        }
        return allProducts;
    }

    public List<Product> getProductsSubstitutes(char grade, Subcategory subcategory) {
        return productRepository.findProductsByGradeIsLessThanAndSubcategory(grade, subcategory);
    }

    public boolean isValida() {
        return true;
    }
}
