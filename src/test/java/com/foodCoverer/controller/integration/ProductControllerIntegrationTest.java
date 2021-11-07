package com.foodCoverer.controller.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foodCoverer.model.*;
import com.foodCoverer.repository.SubcategoryRepository;
import com.foodCoverer.service.ProductService;
import com.foodCoverer.session.SessionManager;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SubcategoryRepository subcategoryRepository;

    @MockBean
    private SessionManager sessionManager;


    private MockMultipartFile getProductRequestPart() throws Exception {
        byte[] produtAsBytes = objectMapper.writeValueAsBytes(new Product());
        MockMultipartFile productJson = new MockMultipartFile("product", null,
                "application/json", produtAsBytes);

        return productJson;
    }

    @Test
    public void getAllProductsShouldReturnOk() throws Exception {
        mockMvc.perform(get("/products/all")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }


    @Test
    public void createProductsShouldReturnBadRequest() throws Exception {

        MockMultipartFile mockMultipartFile = new MockMultipartFile("file", new byte[]{});

        mockMvc.perform(MockMvcRequestBuilders.multipart("/products/create/withFile")
                .file(mockMultipartFile))
                .andExpect(status().isBadRequest())
                .andExpect(status().reason(containsString("Required request part 'product' is not present")));

    }

    @Test
    public void createProductsShouldReturnForbidden() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.multipart("/products/create/withFile")
                .file(getProductRequestPart()))
                .andExpect(status().isForbidden());

    }

    @Test
    public void createProductsShouldReturnOk() throws Exception {
        when(sessionManager.isLoggedIn()).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.multipart("/products/create/withFile")
                .file(getProductRequestPart()))
                .andExpect(status().isOk());
    }

    @Test
    public void createProductWithLinkShouldReturnForbidden() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.multipart("/products/create/withLink")
                .file(getProductRequestPart()))
                .andExpect(status().isForbidden());
    }

    @Test
    public void createProductWithLinkShouldReturnOk() throws Exception {
        when(sessionManager.isLoggedIn()).thenReturn(true);

        byte[] nutriDataAsBytes = objectMapper.writeValueAsBytes(new NutritionData());
        MockMultipartFile nutritionDataJson = new MockMultipartFile("nutriData", null,
                "application/json", nutriDataAsBytes);

        mockMvc.perform(MockMvcRequestBuilders.multipart("/products/create/withLink")
                .file(getProductRequestPart()).file(nutritionDataJson))
                .andExpect(status().isOk());
    }

    @Test
    public void updateWithFileShouldReturnForbidden() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.multipart("/products/update/withFile")
                .file(getProductRequestPart()))
                .andExpect(status().isForbidden());
    }

    @Test
    public void updateWithFileShouldReturnNoContent() throws Exception {
        when(sessionManager.isAuthorized(any())).thenReturn(true);
        when(productService.checkIfExists(any())).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders.multipart("/products/update/withFile")
                .file(getProductRequestPart()))
                .andExpect(status().isNoContent());
    }

    @Test
    public void updateWithFileShouldReturnOk() throws Exception {
        when(sessionManager.isAuthorized(any())).thenReturn(true);
        when(productService.checkIfExists(any())).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.multipart("/products/update/withFile")
                .file(getProductRequestPart()))
                .andExpect(status().isOk());
    }

    @Test
    public void updateWithLinkShouldReturnForbidden() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.multipart("/products/update/withLink")
                .file(getProductRequestPart()))
                .andExpect(status().isForbidden());
    }

    @Test
    public void updateWithLinkShouldReturnNoContent() throws Exception {
        when(sessionManager.isAuthorized(any())).thenReturn(true);
        when(productService.checkIfExists(any())).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders.multipart("/products/update/withLink")
                .file(getProductRequestPart()))
                .andExpect(status().isNoContent());
    }

    @Test
    public void updateWithLinkShouldReturnOk() throws Exception {
        when(sessionManager.isAuthorized(any())).thenReturn(true);
        when(productService.checkIfExists(any())).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.multipart("/products/update/withLink")
                .file(getProductRequestPart()))
                .andExpect(status().isOk());
    }

    @Test
    public void getProductByCategoryShouldReturnOk() throws Exception {
        String name = "mock-product-name";
        Category mockCategory = new Category();
        mockCategory.setName(name);
        List<Product> mockList = new ArrayList<>();
        Product mockProduct = new Product();
        mockProduct.setProductName(name);
        mockList.add(mockProduct);
        when(productService.getProductsByCategory(any())).thenReturn(mockList);
        MvcResult result = mockMvc.perform(post("/products/category")
                .content(objectMapper.writeValueAsString(mockCategory))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        String content = result.getResponse().getContentAsString();
        assertTrue(content.contains(name));

    }

    @Test
    public void deleteProductShouldReturnForbidden() throws Exception {
        Product mockProduct = new Product();
        mockMvc.perform(delete("/products/delete")
                .content(objectMapper.writeValueAsString(mockProduct))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    public void deleteProductShouldReturnNoContent() throws Exception {
        when(sessionManager.isAuthorized(any())).thenReturn(true);
        Product mockProduct = new Product();
        mockMvc.perform(delete("/products/delete")
                .content(objectMapper.writeValueAsString(mockProduct))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    public void deleteProductShouldReturnOk() throws Exception {
        Product mockProduct = new Product();
        when(sessionManager.isAuthorized(any())).thenReturn(true);
        when(productService.checkIfExists(any())).thenReturn(true);

        mockMvc.perform(delete("/products/delete")
                .content(objectMapper.writeValueAsString(mockProduct))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(productService).deleteProduct((any()));
    }

    @Test
    public void getProductBySubcategoryShouldReturnOk() throws Exception {
        Subcategory mockSubcategory = new Subcategory();

        mockMvc.perform(post("/products/subcategory")
                .content(objectMapper.writeValueAsString(mockSubcategory))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    @Test
    public void getSubstitutesShouldReturnOk() throws Exception {
        Subcategory subcategory = new Subcategory();

        mockMvc.perform(post("/products/substitutes/grade=e")
                .content(objectMapper.writeValueAsString(subcategory))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        verify(productService).getProductsSubstitutes(eq('e'), any());

    }

    @Test
    public void getNotVerifiedProductsShouldReturnForbidden() throws Exception {
        mockMvc.perform(get("/products/notVerified")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    public void getNotVerifiedProductsShouldReturnOk() throws Exception {
        when(sessionManager.isAdmin()).thenReturn(true);

        mockMvc.perform(get("/products/notVerified")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void verifyProductShouldReturnForbidden() throws Exception {
        when(sessionManager.isAdmin()).thenReturn(false);

        mockMvc.perform(patch("/products/verify")
                .content(objectMapper.writeValueAsString(new Product()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }


    @Test
    public void verifyProductShouldReturnNoContent() throws Exception {
        when(productService.toggleVerifyProduct(any())).thenReturn(false);
        when(sessionManager.isAdmin()).thenReturn(true);

        mockMvc.perform(patch("/products/verify")
                .content(objectMapper.writeValueAsString(new Product()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    public void verifyProductShouldReturnOk() throws Exception {
        when(productService.toggleVerifyProduct(any())).thenReturn(true);
        when(sessionManager.isAdmin()).thenReturn(true);

        mockMvc.perform(patch("/products/verify")
                .content(objectMapper.writeValueAsString(new Product()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void byUserShouldReturnForbidden() throws Exception {
        mockMvc.perform(get("/products/byUser")
                .content(objectMapper.writeValueAsString(new Product()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    public void byUserShouldReturnOk() throws Exception {
        when(sessionManager.isLoggedIn()).thenReturn(true);
        mockMvc.perform(get("/products/byUser")
                .content(objectMapper.writeValueAsString(new Product()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}