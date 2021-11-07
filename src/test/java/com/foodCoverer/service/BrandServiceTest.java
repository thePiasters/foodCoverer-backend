package com.foodCoverer.service;

import com.foodCoverer.model.Brand;
import com.foodCoverer.model.User;
import com.foodCoverer.model.Product;
import com.foodCoverer.repository.BrandRepository;
import com.foodCoverer.repository.ProductRepository;
import com.foodCoverer.session.SessionManager;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.xml.sax.SAXException;

import javax.validation.Validator;
import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
class BrandServiceTest {

    @Mock
    private SessionManager sessionManager;

    @Mock
    private BrandRepository brandRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private Validator validator;


    @InjectMocks
    private BrandService brandService;


  private User createMockUser(){
     return new User("test-id", "test-name", "test-email", "test-picture");
  }

    @Test
    public void getAllBrandsSortedShouldReturnSortedCollection() {
        List<Brand> brands = new ArrayList<>();
        Brand brand1 = new Brand();
        Brand brand2 = new Brand();
        Brand brand3 = new Brand();

        brand1.setName("c");
        brand2.setName("a");
        brand3.setName("b");

        brands.add(brand1);
        brands.add(brand2);
        brands.add(brand3);

        when(brandRepository.findAll()).thenReturn(brands);
        var result = brandService.getAllBrandsSorted();

        Assert.assertEquals(result.get(0), brand2);
        Assert.assertEquals(result.get(1), brand3);
        Assert.assertEquals(result.get(2), brand1);

    }

    @Test
    public void getBrandsByLoggedInUser() {

        List<Brand> mockBrandsList = new ArrayList<>();
        when(brandRepository.findBrandByUser(any())).thenReturn(mockBrandsList);
        Assert.assertEquals(mockBrandsList, brandService.getBrandsByLoggedInUser());
    }

    @Test
    public void deleteBrandShouldRemoveBrandFromProduct() {

        Brand mockBrand = new Brand();
        List<Product> mockProducts = new ArrayList<>();
        Product mockProduct = new Product();
        mockProduct.setBrand(mockBrand);
        mockProducts.add(mockProduct);
        when(productRepository.findProductsByBrand(mockBrand)).thenReturn(mockProducts);
        brandService.deleteBrand(mockBrand);
        verify(productRepository).saveAndFlush(mockProduct);
        verify(brandRepository).delete(mockBrand);
        assertNull(mockProduct.getBrand());
    }


    @Test
    public void isAuthorizedShouldReturnTrue() {
        when(sessionManager.isAuthorized(any())).thenReturn(true);
        assertTrue(brandService.isAuthorizedToModify(new Brand()));
    }


    @Test
    public void checkIfExistsByUUIDShouldReturnTrue() {
        UUID mockUUID = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        when(brandRepository.existsById(mockUUID)).thenReturn(true);
        Brand mockBrand = new Brand();
        mockBrand.setId(mockUUID);
        assertTrue(brandService.checkIfExistsByUUID(mockBrand));
    }

    @Test
    public void checkIfExistsByNameShouldReturnTrue() {
        String brandName = "test-brand-name";
        Brand firstMockBrand = new Brand();
        firstMockBrand.setName(brandName);
        Brand secondMockBrand = new Brand();
        secondMockBrand.setName(brandName);
        Optional<Brand> brandOptional = Optional.of(firstMockBrand);
        when(brandRepository.findByName(brandName)).thenReturn(brandOptional);
        assertTrue(brandService.checkIfExistsByName(secondMockBrand));
    }
    @Test
    public void createNewBrandShouldSetAttributes(){
        User mockUser = createMockUser();
        Brand mockBrand = new Brand();
        when(sessionManager.getLoggedInUser()).thenReturn(mockUser);
        brandService.createNewBrand(mockBrand);
        verify(brandRepository).saveAndFlush(any());
        assertNotNull(mockBrand.getId());
        assertEquals(mockBrand.getUser(), mockUser);
        assertFalse(mockBrand.isVerified());
    }

    @Test
    public void verifyBrandShouldSetVerifiedAsTrue(){
        Brand mockBrand = new Brand();
        brandService.verifyBrand(mockBrand);
        verify(brandRepository).saveAndFlush(any());
        assertTrue(mockBrand.isVerified());
    }
    @Test
    public void saveBrandShouldSaveAndFlush() throws IOException, SAXException {
        Brand mockBrand = new Brand();
        when(validator.validate(mockBrand)).thenReturn(new HashSet<>());
        mockBrand.setName("Test-name");
        brandService.saveBrand(mockBrand);
        verify(brandRepository).saveAndFlush(any());
    }
}