package com.foodCoverer.service;

import com.foodCoverer.model.*;
import com.foodCoverer.repository.CategoryRepository;
import com.foodCoverer.repository.ProductRepository;
import com.foodCoverer.repository.SubcategoryRepository;
import com.foodCoverer.session.SessionManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
class ProductServiceTest {

    @Mock
    private ImageService imageService;

    @Mock
    private NutritionDataService nutritionDataService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private SubcategoryRepository subcategoryRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    SessionManager sessionManager;

    @InjectMocks
    private ProductService productService;

    private static final String EXAMPLE_LINK = "https://france-export-fv-online.com/4827-large_default/chips-bolognaise-lay-s.jpg";
    private static final String EXAMPLE_PATH = "/image/test-file.jpg";
    private static final long PRODUCT_ID = Long.valueOf("343434");
    private static final String SUBCATEGORY_NAME = "test-subcategory-name";
    private static final String CATEGORY_NAME = "test-category-name";

    @Test
    void shouldReturnNotVerifiedProducts() {
        List<Product> mockProductsList = new ArrayList<>();
        when(productRepository.findProductsByIsVerified(false)).thenReturn(mockProductsList);
        assertEquals(mockProductsList, productService.findNotVerifiedProducts());
    }

    @Test
    void shouldReturnNotAllProducts() {
        List<Product> mockProductsList = new ArrayList<>();
        when(productRepository.findAll()).thenReturn(mockProductsList);
        assertEquals(mockProductsList, productService.getAllProducts());
    }

    @Test
    void checkIfExistsShouldReturnTrue() {
        when(productRepository.existsById(any())).thenReturn(true);
        assertTrue(productService.checkIfExists(new Product()));
    }

    @Test
    void shouldCallDeleteProduct() {
        when(productRepository.existsById(any())).thenReturn(true);
        productService.deleteProduct(new Product());
        verify(productRepository).delete(any());
    }

    @Test
    void shouldToggleProductVerifyStatus() {
        Product product = new Product();
        product.setVerified(false);
        when(productRepository.existsById(any())).thenReturn(true);
        assertTrue(productService.toggleVerifyProduct(product));
        assertTrue(product.isVerified());
        verify(productRepository).saveAndFlush(product);
    }

    @Test
    void shouldReturnProductsCreatedByUser() {
        var mockProductsList = new ArrayList<Product>();
        when(productRepository.findProductsByUser(any())).thenReturn(mockProductsList);
        assertEquals(mockProductsList, productService.getProductByUser(new User()));
    }

    @Test
    void shouldCreateProductWithImageSoureExternalLink() {
        Product mockProduct = new Product();

        Image mockImage = new Image();
        mockImage.setImageUrl(EXAMPLE_LINK);

        User mockUser = new User();
        NutritionData mockNutritionData = new NutritionData();

        when(imageService.createImageLink(eq(EXAMPLE_LINK), any())).thenReturn(mockImage);
        when(sessionManager.getLoggedInUser()).thenReturn(mockUser);
        when(productRepository.saveAndFlush(any())).thenReturn(mockProduct);
        when(nutritionDataService.createNutritionData(any())).thenReturn(mockNutritionData);

        var result = productService.createProductWithLink(mockProduct, Optional.of(EXAMPLE_LINK), Optional.of(mockNutritionData));
        assertEquals(mockUser, result.getUser());
        assertEquals(mockImage, result.getImage());
        assertEquals(mockNutritionData, result.getNutritionData());
    }

    @Test
    void shouldCreateProductWithImageSoureServerFile() throws IOException {
        Product mockProduct = new Product();

        Image mockImage = new Image();
        mockImage.setImageUrl(EXAMPLE_PATH);

        User mockUser = new User();
        NutritionData mockNutritionData = new NutritionData();

        MultipartFile mockMultipartFile = new MockMultipartFile("name", new byte[]{});
        when(imageService.createServerImage(eq(mockMultipartFile), any())).thenReturn(mockImage);
        when(sessionManager.getLoggedInUser()).thenReturn(mockUser);
        when(productRepository.saveAndFlush(any())).thenReturn(mockProduct);
        when(nutritionDataService.createNutritionData(any())).thenReturn(mockNutritionData);
        var result = productService.createProductWithFile(mockProduct, Optional.of(mockMultipartFile), Optional.of(mockNutritionData));
        assertEquals(mockUser, result.getUser());
        assertEquals(mockImage, result.getImage());
        assertEquals(mockNutritionData, result.getNutritionData());


    }

    @Test
    void shouldUpdateProductWithImageSoureServerFile() throws IOException {
        Product mockProduct = new Product();
        mockProduct.setProductId(PRODUCT_ID);

        Image mockImage = new Image();
        mockImage.setImageUrl(EXAMPLE_PATH);

        User mockUser = new User();
        NutritionData mockNutritionData = new NutritionData();

        MultipartFile mockMultipartFile = new MockMultipartFile("name", new byte[]{});
        when(imageService.updateServerImage(eq(mockMultipartFile), any())).thenReturn(mockImage);
        when(sessionManager.getLoggedInUser()).thenReturn(mockUser);
        when(productRepository.saveAndFlush(any())).thenReturn(mockProduct);
        when(nutritionDataService.updateProductNutritionData(eq(PRODUCT_ID), any())).thenReturn(mockNutritionData);

        var result = productService.updateProductWithPictureFile(mockProduct, Optional.of(mockNutritionData), Optional.of(mockMultipartFile));
        assertEquals(mockUser, result.getUser());
        assertEquals(mockImage, result.getImage());
        assertEquals(mockNutritionData, result.getNutritionData());

    }

    @Test
    void shouldUpdateProductWithImageSoureExternalLink() {
        Product mockProduct = new Product();
        mockProduct.setProductId(PRODUCT_ID);

        Image mockImage = new Image();
        mockImage.setImageUrl(EXAMPLE_LINK);

        User mockUser = new User();
        NutritionData mockNutritionData = new NutritionData();

        when(imageService.updateImageLink(eq(EXAMPLE_LINK), any())).thenReturn(mockImage);
        when(sessionManager.getLoggedInUser()).thenReturn(mockUser);
        when(productRepository.saveAndFlush(any())).thenReturn(mockProduct);
        when(nutritionDataService.updateProductNutritionData(eq(PRODUCT_ID), any())).thenReturn(mockNutritionData);

        var result = productService.updatePictureWithPictureLink(mockProduct, Optional.of(mockNutritionData), Optional.of(EXAMPLE_LINK));
        assertEquals(mockUser, result.getUser());
        assertEquals(mockImage, result.getImage());
        assertEquals(mockNutritionData, result.getNutritionData());
    }

    @Test
    void shouldReturnProductsBySubcategory() {
        List<Product> mockProductList = new ArrayList<>();
        Subcategory mockSubcategory = new Subcategory();
        mockSubcategory.setName(SUBCATEGORY_NAME);

        when(productRepository.findBySubcategory(mockSubcategory)).thenReturn(mockProductList);
        assertEquals(mockProductList, productService.findProductsBySubcategory(mockSubcategory));
    }

    @Test
    void shouldReturnProductsByCategory() {
        Category mockCategory = new Category();
        mockCategory.setName(CATEGORY_NAME);
        Subcategory mockSubcategory = new Subcategory();
        mockSubcategory.setName(SUBCATEGORY_NAME);

        Product mockProduct = new Product();

        when(subcategoryRepository.findAllByCategory(mockCategory)).thenReturn(List.of(mockSubcategory));
        when(productRepository.findBySubcategory(mockSubcategory)).thenReturn(List.of(mockProduct));

        assertTrue(productService.getProductsByCategory(mockCategory).contains(mockProduct));
    }

    @Test
    void shouldReturnProductSubstitutes() {
        char grade = 'c';
        Subcategory mockSubcategory = new Subcategory();
        mockSubcategory.setName(SUBCATEGORY_NAME);

        List<Product> mockProductList = new ArrayList<>();
        when(productRepository.findProductsByGradeIsLessThanAndSubcategory(grade, mockSubcategory)).thenReturn(mockProductList);
        assertEquals(productService.getProductsSubstitutes(grade, mockSubcategory), mockProductList);
    }


}