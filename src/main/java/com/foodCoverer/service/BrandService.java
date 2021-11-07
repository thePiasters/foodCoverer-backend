package com.foodCoverer.service;

import com.foodCoverer.model.Brand;
import com.foodCoverer.model.Product;
import com.foodCoverer.repository.BrandRepository;
import com.foodCoverer.repository.ProductRepository;
import com.foodCoverer.session.SessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Validator;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BrandService {

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private SessionManager sessionManager;

    @Autowired
    private Validator validator;


    public List<Brand> getAllBrandsSorted() {
        List<Brand> brands = brandRepository.findAll();
        Collections.sort(brands, (Brand b1, Brand b2) -> {
            return b1.getName().compareToIgnoreCase(b2.getName());
        });
        return brands;
    }

    public List<Brand> getBrandsByLoggedInUser() {
        return brandRepository.findBrandByUser(sessionManager.getLoggedInUser());
    }

    public void deleteBrand(Brand brand) {
        for (Product product : productRepository.findProductsByBrand(brand)) {
            product.setBrand(null);
            productRepository.saveAndFlush(product);
        }
        brandRepository.delete(brand);
    }

    public boolean isAuthorizedToModify(Brand brand) {

        if (brand.isVerified() && !sessionManager.isAdmin()) {
            return false;
        }
        return sessionManager.isAuthorized(brand.getUser());

    }

    public boolean checkIfExistsByUUID(Brand brand) {
        return brandRepository.existsById(brand.getId());
    }

    public boolean checkIfExistsByName(Brand brand) {
        String name = brand.getName();
        Optional<Brand> brandOptional = brandRepository.findByName(name);
        return brandOptional.isPresent();
    }

    public boolean createNewBrand(Brand brand) {

        UUID uuid = UUID.randomUUID();
        brand.setId(uuid);
        brand.setVerified(sessionManager.isAdmin());
        brand.setUser(sessionManager.getLoggedInUser());

        return saveBrand(brand);
    }

    public void verifyBrand(Brand brand) {
        brand.setVerified(true);
        brandRepository.saveAndFlush(brand);
    }

    public boolean saveBrand(Brand brand) {
        if (validator.validate(brand).isEmpty()) {
            brandRepository.saveAndFlush(brand);
            return true;
        } else {
            System.out.println(validator.validate(brand));
            return false;
        }
    }


}
