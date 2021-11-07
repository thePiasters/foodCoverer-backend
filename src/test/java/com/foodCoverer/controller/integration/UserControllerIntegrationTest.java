package com.foodCoverer.controller.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foodCoverer.model.Role;
import com.foodCoverer.model.User;
import com.foodCoverer.repository.UserRepository;
import com.foodCoverer.service.UserService;
import com.foodCoverer.session.SessionManager;
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


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private SessionManager sessionManager;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String TOKEN = "TEST_TOKEN";
    private static final String USER_AS_STRING = "USER";

    @Test
    public void allShouldReturnForbidden() throws Exception {

        mockMvc.perform(get("/users/all")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TOKEN)).andExpect(status().isForbidden());
    }

    @Test
    public void allShouldReturnOk() throws Exception {

        when(sessionManager.isAdmin()).thenReturn(true);

        mockMvc.perform(get("/users/all")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TOKEN)).andExpect(status().isOk());

        verify(userRepository).findAll();
    }

    @Test
    public void disableReturnForbidden() throws Exception {
        when(sessionManager.isAdmin()).thenReturn(false);

        mockMvc.perform(patch("/users/disable")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new User())))
                .andExpect(status().isForbidden());
    }

    @Test
    public void disableReturnOk() throws Exception {
        when(sessionManager.isAdmin()).thenReturn(true);

        mockMvc.perform(patch("/users/disable")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new User())))
                .andExpect(status().isOk());

        verify(userService).disableAccount(any());
    }

    @Test
    public void enableReturnForbidden() throws Exception {
        when(sessionManager.isAdmin()).thenReturn(false);

        mockMvc.perform(patch("/users/enable")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new User())))
                .andExpect(status().isForbidden());
    }

    @Test
    public void enableReturnOk() throws Exception {
        when(sessionManager.isAdmin()).thenReturn(true);

        mockMvc.perform(patch("/users/enable")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new User())))
                .andExpect(status().isOk());

        verify(userService).enableAccount(any());
    }


    @Test
    public void authenticateShouldReturnOk() throws Exception {
        when(userService.authenticate(TOKEN)).thenReturn(Role.USER);

        MvcResult result = mockMvc.perform(post("/users/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TOKEN)).andExpect(status().isOk()).andReturn();

        assertTrue(result.getResponse().getContentAsString().contains(USER_AS_STRING));
    }

    @Test
    public void elevateReturnForbidden() throws Exception {
        when(sessionManager.isAdmin()).thenReturn(false);

        mockMvc.perform(patch("/users/elevate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new User())))
                .andExpect(status().isForbidden());
    }

    @Test
    public void elevateShouldReturnOk() throws Exception {
        when(sessionManager.isAdmin()).thenReturn(true);

        mockMvc.perform(patch("/users/elevate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new User())))
                .andExpect(status().isOk());

        verify(userService).elevate(any());
    }

    @Test
    public void logOutShouldReturnOk() throws Exception {
        mockMvc.perform(get("/users/logOut").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(sessionManager).endSession();

    }


}