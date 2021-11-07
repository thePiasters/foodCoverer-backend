package com.foodCoverer.service;

import com.foodCoverer.model.Role;
import com.foodCoverer.model.User;
import com.foodCoverer.repository.UserRepository;
import com.foodCoverer.session.SessionManager;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.json.webtoken.JsonWebSignature;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private VerifyService verifyService;

    @InjectMocks
    private UserService userService;

    @Mock
    private SessionManager sessionManager;

    private static final String ID = "457175939507097";
    private static final String TOKEN = "mock-token";



    @Test
    void shouldEnableUserAccount() {
        User mockUser = new User();
        mockUser.setRole(Role.INACTIVE);
        userService.enableAccount(mockUser);
        verify(userRepository).saveAndFlush(mockUser);
        assertEquals(Role.USER, mockUser.getRole());
    }

    @Test
    void shouldElevateUSer() {
        User mockUser = new User();
        mockUser.setRole(Role.USER);
        userService.elevate(mockUser);
        verify(userRepository).saveAndFlush(mockUser);
        assertEquals(Role.ADMIN, mockUser.getRole());
    }

    @Test
    void shouldDisableUserAccount() {
        User mockUser = new User();
        mockUser.setRole(Role.USER);
        userService.disableAccount(mockUser);
        verify(userRepository).saveAndFlush(mockUser);
        assertEquals(Role.INACTIVE, mockUser.getRole());
    }


    @Test
    void authenticateShouldReturnUserRoleOfNewUser() throws GeneralSecurityException, IOException {

        GoogleIdToken.Payload payload = new GoogleIdToken.Payload();
        payload.setSubject(ID);
        payload.setEmailVerified(true);

        GoogleIdToken idToken = new GoogleIdToken(new JsonWebSignature.Header(),payload, new byte[] {},new byte[] {});
        when(verifyService.verifyUserToken(anyString())).thenReturn(idToken);
        when(userRepository.findById(ID)).thenReturn(Optional.ofNullable(null));

        assertEquals(Role.USER, userService.authenticate(TOKEN));
        verify(userRepository).saveAndFlush(any());
        verify(sessionManager).startSession(any());

    }
    @Test
    void authenticateShouldReturnUserRoleOfFoundUser() throws GeneralSecurityException, IOException {
        GoogleIdToken.Payload payload = new GoogleIdToken.Payload();
        payload.setSubject(ID);
        payload.setEmailVerified(true);

        User mockUser =  new User();
        mockUser.setId(ID);
        mockUser.setRole(Role.USER);

        GoogleIdToken idToken = new GoogleIdToken(new JsonWebSignature.Header(),payload, new byte[] {},new byte[] {});
        when(verifyService.verifyUserToken(anyString())).thenReturn(idToken);
        when(userRepository.findById(ID)).thenReturn(Optional.ofNullable(mockUser));

        assertEquals(Role.USER, userService.authenticate(TOKEN));
        verify(sessionManager).startSession(mockUser);
    }
    @Test
    void authenticateShouldReturnUserRoleNoneForUnVerifiedEmail() throws GeneralSecurityException, IOException {
        GoogleIdToken.Payload payload = new GoogleIdToken.Payload();
        payload.setSubject(ID);
        payload.setEmailVerified(false);


        GoogleIdToken idToken = new GoogleIdToken(new JsonWebSignature.Header(),payload, new byte[] {},new byte[] {});
        when(verifyService.verifyUserToken(anyString())).thenReturn(idToken);
        when(userRepository.findById(ID)).thenReturn(Optional.ofNullable(null));

        assertEquals(Role.NONE, userService.authenticate(TOKEN));
        verify(sessionManager, never()).startSession(any());
    }

    @Test
    void authenticateShouldReturnNoneforNullToken() throws GeneralSecurityException, IOException {
        when(verifyService.verifyUserToken(anyString())).thenReturn(null);
        assertEquals(Role.NONE, userService.authenticate(TOKEN));
    }



}