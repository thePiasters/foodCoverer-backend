package com.foodCoverer.controller.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foodCoverer.model.Brand;
import com.foodCoverer.service.BrandService;
import com.foodCoverer.session.SessionManager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.validation.Validator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class BrandsControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BrandService brandService;

    @MockBean
    private SessionManager sessionManager;

    @Autowired
    private ObjectMapper objectMapper;

    @Mock
    private Validator validator;


    @Test
    public void testGetAllBrands() throws Exception {
        List<Brand> brands = new ArrayList<>();
        Brand brand = new Brand();
        brand.setName("Example-brand");
        brands.add(brand);
        when(brandService.getAllBrandsSorted()).thenReturn(brands);

        mockMvc.perform(get("/brands/all").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is(brand.getName())));
    }

    @Test
    public void testDeleteBrandBadRequest() throws Exception {

        mockMvc.perform(delete("/brands/delete").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

    }

    @Test
    public void testDeleteBrandForNonAuthenticatedRequest() throws Exception {
        Brand brand = new Brand();
        mockMvc.perform(delete("/brands/delete")
                .content(objectMapper.writeValueAsString(brand))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testDeleteNonExistingBrand() throws Exception {
        when(brandService.isAuthorizedToModify(any())).thenReturn(true);
        mockMvc.perform(delete("/brands/delete")
                .content(objectMapper.writeValueAsString(new Brand()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteBrand() throws Exception {
        when(brandService.isAuthorizedToModify(any())).thenReturn(true);
        when(brandService.checkIfExistsByUUID(any())).thenReturn(true);
        mockMvc.perform(delete("/brands/delete")
                .content(objectMapper.writeValueAsString(new Brand()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testUpdateBrandWithFailedValidation() throws Exception {
        when(brandService.isAuthorizedToModify(any())).thenReturn(true);
        when(brandService.checkIfExistsByUUID(any())).thenReturn(true);
        Brand brand = new Brand();
        mockMvc.perform(patch("/brands/update")
                .content(objectMapper.writeValueAsString(brand))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
        Mockito.verify(brandService).saveBrand(any());
    }

    @Test
    public void testUpdateBrandShouldReturnOk() throws Exception {
        when(brandService.isAuthorizedToModify(any())).thenReturn(true);
        when(brandService.checkIfExistsByUUID(any())).thenReturn(true);
        when(validator.validate(any())).thenReturn(new HashSet<>());
        Brand brand = new Brand();
        mockMvc.perform(patch("/brands/update")
                .content(objectMapper.writeValueAsString(brand))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
        Mockito.verify(brandService).saveBrand(any());
    }


    @Test
    public void testAddBrandNotLoggedInUser() throws Exception {

        mockMvc.perform(post("/brands/add")
                .content(objectMapper.writeValueAsString(new Brand()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());

    }

    @Test
    public void testAddBrandAlreadyExisting() throws Exception {
        when(sessionManager.isLoggedIn()).thenReturn(true);
        when(brandService.checkIfExistsByName(any())).thenReturn(true);

        mockMvc.perform(post("/brands/add")
                .content(objectMapper.writeValueAsString(new Brand()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());
    }

    @Test
    public void testAddNewBrandValidationFailed() throws Exception {
        when(sessionManager.isLoggedIn()).thenReturn(true);
        when(brandService.checkIfExistsByName(any())).thenReturn(false);

        mockMvc.perform(post("/brands/add")
                .content(objectMapper.writeValueAsString(new Brand()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

        Mockito.verify(brandService).createNewBrand(any());
    }

    @Test
    public void testAddNewBrandShouldReturnOk() throws Exception {
        when(sessionManager.isLoggedIn()).thenReturn(true);
        when(brandService.checkIfExistsByName(any())).thenReturn(false);
        when(validator.validate(any())).thenReturn(new HashSet<>());

        mockMvc.perform(post("/brands/add")
                .content(objectMapper.writeValueAsString(new Brand()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

        Mockito.verify(brandService).createNewBrand(any());
    }

    @Test
    public void testGetBrandsByUsershouldReturnForbidden() throws Exception {

        mockMvc.perform(get("/brands/byUser")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testGetBrandsByUserShouldReturnOK() throws Exception {
        when(sessionManager.isLoggedIn()).thenReturn(true);
        mockMvc.perform(get("/brands/byUser")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testVerifyShouldReturnForbidden() throws Exception {

        mockMvc.perform(patch("/brands/verify")
                .content(objectMapper.writeValueAsString(new Brand()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testVerifyShouldReturnNoContent() throws Exception {
        when(sessionManager.isAdmin()).thenReturn(true);
        when(brandService.checkIfExistsByUUID(any())).thenReturn(false);
        mockMvc.perform(patch("/brands/verify")
                .content(objectMapper.writeValueAsString(new Brand()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testVerifyShouldReturnOk() throws Exception {
        when(sessionManager.isAdmin()).thenReturn(true);
        when(brandService.checkIfExistsByUUID(any())).thenReturn(true);
        mockMvc.perform(patch("/brands/verify")
                .content(objectMapper.writeValueAsString(new Brand()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

}